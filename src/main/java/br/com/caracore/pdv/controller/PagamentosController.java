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
import br.com.caracore.pdv.service.exception.CpfExistenteException;
import br.com.caracore.pdv.service.exception.CpfInvalidoException;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.NomeExistenteException;
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
		ModelAndView mv = new ModelAndView("redirect:/pagamento/forma-pagamento");
		try {
			if (Util.validar(codigoPagamento)) {
				pagamento = pagamentoService.pesquisarPorCodigo(codigoPagamento);
				cliente = new Cliente(cpf, nome);
				cliente = pagamentoService.salvarCliente(cliente);
				pagamento.setCpf(cpf);
				pagamento.setCliente(cliente);
				attributes.addFlashAttribute("mensagem", "Cliente cadastrado com sucesso!");
			}
		} catch (CpfInvalidoException ex) {
			attributes.addFlashAttribute("error", "CPF inválido!");
		} catch (CpfExistenteException ex) {
			attributes.addFlashAttribute("error", "CPF já cadastrado!");
		} catch (NomeExistenteException ex) {
			attributes.addFlashAttribute("error", "Nome já existente!");
		}
		mv.addObject(pagamento);
		return mv;
	}
		
	@PostMapping("/pagamento")
	public ModelAndView formaPagamento(@Valid Pagamento pagamento, Errors errors, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("pagamento/forma-pagamento");
		mv.addObject(pagamento);
		return mv;
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
			if (!Util.validar(pagamento)) {
				pagamento = new Pagamento(cliente, venda, venda.getSubTotal(), venda.getDescontoTotal());
			}
			pagamentoService.salvar(pagamento, cliente);
			mv.addObject(pagamento);
			return mv;
		} catch (DescontoInvalidoException ex) {
			attributes.addFlashAttribute("error", "Desconto inválido.");
			return new ModelAndView("redirect:/vendas/novo");
		}
	}
			
}

