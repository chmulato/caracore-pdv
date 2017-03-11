package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.service.PagamentoService;
import br.com.caracore.pdv.service.VendaService;
import br.com.caracore.pdv.service.exception.CpfInvalidoException;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.NomeExistenteException;
import br.com.caracore.pdv.service.exception.ValorInvalidoException;
import br.com.caracore.pdv.util.Util;

@Controller
@RequestMapping("/pagamentos")
public class PagamentosController {
	
	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private VendaService vendaService;

	@GetMapping("/forma-pagamento")
	public ModelAndView pesquisarPagamento(@Valid Pagamento pagamento, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("pagamento/forma-pagamento");
		mv.addObject(pagamento);
		return mv;
	}
	
	@PostMapping("/cliente")
	public ModelAndView salvarCliente(
			@RequestParam(value="codigoPagamento", required=false) Long codigoPagamento, 
			@RequestParam(value="cpf", required=false) String cpf,
			@RequestParam(value="nome", required=false) String nome,
			RedirectAttributes attributes) {
		Pagamento pagamento = null;
		Cliente cliente = null;
		ModelAndView mv = new ModelAndView("redirect:/pagamentos/forma-pagamento");
		try {
			if (Util.validar(codigoPagamento)) {
				pagamento = pagamentoService.pesquisarPorCodigo(codigoPagamento);
				cliente = new Cliente(cpf, nome);
				cliente = pagamentoService.salvarCliente(cliente);
				pagamento.setCpf(cliente.getCpf());
				pagamento = pagamentoService.salvar(pagamento, cliente);
				if (cliente.isThereIs()) {
					attributes.addFlashAttribute("info", "CPF já cadastrado!");
				} else {
					attributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
				}
			}
		} catch (CpfInvalidoException ex) {
			attributes.addFlashAttribute("error", "CPF inválido!");
		} catch (NomeExistenteException ex) {
			attributes.addFlashAttribute("error", "Nome já existente!");
		}
		mv.addObject(pagamento);
		return mv;
	}
		
	@PostMapping("/pagamento")
	public ModelAndView pagamento(@Valid Pagamento pagamento, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return pesquisarPagamento(pagamento, attributes);
		}
		ModelAndView mv = new ModelAndView("redirect:/pagamentos/forma-pagamento");
		try {
			if (Util.validar(pagamento.getCpf())) {
				String cpf = pagamento.getCpf();
				pagamento.setCpf(Util.removerFormatoCpf(cpf));
			}
			pagamento = pagamentoService.salvar(pagamento);
			attributes.addFlashAttribute("mensagem", "Pago com sucesso!");
			mv.addObject(pagamento);
			return mv;
		} catch (DescontoInvalidoException ex) {
			errors.rejectValue("desconto", " ", ex.getMessage());
			attributes.addFlashAttribute("error", "Corrigir o desconto!");
		} catch (CpfInvalidoException ex) {
			errors.rejectValue("cpf", " ", ex.getMessage());
			attributes.addFlashAttribute("error", "Corrigir CPF!");
		} catch (ValorInvalidoException ex) {
			errors.rejectValue("dinheiro", " ", ex.getMessage());
			errors.rejectValue("cartao", " ", ex.getMessage());
			errors.rejectValue("cheque", " ", ex.getMessage());
			errors.rejectValue("outros", " ", ex.getMessage());
			attributes.addFlashAttribute("error", "Corrigir valores!");
		}
		mv.addObject(pagamento);
		return pesquisarPagamento(pagamento, attributes);
	}

	@PostMapping("/forma-pagamento")
	public ModelAndView formaPagamento(@Valid Venda venda, Errors errors, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("pagamento/forma-pagamento");
		if (errors.hasErrors()) {
			if (!Util.validar(venda.getCodigo())) {
				attributes.addFlashAttribute("error", "Cesta vazia! Selecione um produto.");
				return new ModelAndView("redirect:/vendas/novo");
			}
			if (!Util.validar(venda.getVendedor())) {
				attributes.addFlashAttribute("error", "Selecione o vendedor.");
				return new ModelAndView("redirect:/vendas/novo");
			}
			if (!venda.validarDesconto()) {
				attributes.addFlashAttribute("error", "Desconto inválido.");
				return new ModelAndView("redirect:/vendas/novo");
			}
		}
		try {
			Cliente cliente = new Cliente();
			vendaService.salvarDescontoTotal(venda.getCodigo(), venda.getDescontoTotal());
			Pagamento pagamento = pagamentoService.pesquisarPorVenda(venda);
			if ((Util.validar(pagamento) && (Util.validar(pagamento.getCpf())))) {
				String cpf = pagamento.getCpf();
				cliente = pagamentoService.buscarCliente(cpf);
			}
			if (!Util.validar(pagamento)) {
				pagamento = new Pagamento(cliente, venda, venda.getSubTotal(), venda.getTotal(), venda.getDescontoTotal());
			} else {
				pagamento.atualizarPagamento(pagamento.getCliente(), venda, venda.getSubTotal(), venda.getTotal(), venda.getDescontoTotal());
			}
			pagamento = pagamentoService.salvar(pagamento, cliente);
			mv.addObject(pagamento);
			return mv;
		} catch (DescontoInvalidoException ex) {
			attributes.addFlashAttribute("error", "Desconto inválido.");
			return new ModelAndView("redirect:/vendas/novo");
		}
	}
			
}

