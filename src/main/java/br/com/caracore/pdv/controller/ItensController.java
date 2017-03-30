package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.service.ItemVendaService;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.QuantidadeInvalidaException;
import br.com.caracore.pdv.util.Util;

@Controller
@RequestMapping("/itens")
public class ItensController {

	@Autowired
	private ItemVendaService itemVendaService;

	@GetMapping("/novo")
	public ModelAndView novo(ItemVenda item) {
		ModelAndView mv = new ModelAndView("item/atualiza-item");
		mv.addObject(item);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid ItemVenda item, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(item);
		}
		try {
			itemVendaService.salvar(item);
			attributes.addFlashAttribute("mensagem", "Item salvo com sucesso!");
			return new ModelAndView("redirect:/vendas/novo");
		} catch (DescontoInvalidoException ex) {
			errors.rejectValue("desconto", " ", ex.getMessage());
			return novo(item);
		} catch (QuantidadeInvalidaException ex) {
			errors.rejectValue("quantidade", " ", ex.getMessage());
			return novo(item);
		}
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		ItemVenda item = itemVendaService.pesquisarPorCodigo(codigo);
		return novo(item);
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		itemVendaService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Item excluído com sucesso!");
		return "redirect:/vendas/novo";
	}

	@RequestMapping(value = "/{codigo}", method = RequestMethod.PUT)
	public String atualizar(@PathVariable("codigo") Long codigo, 
			@RequestParam(value="desconto", required=false) String desconto,
			@RequestParam(value="dinheiro", required=false) String dinheiro,
			@RequestParam(value="quantidade", required=false) String quantidade,
			RedirectAttributes attributes) {
		try {
			if (Util.validar(codigo) && Util.validar(desconto)) {
				itemVendaService.atualizar(codigo, desconto, dinheiro, quantidade);
				attributes.addFlashAttribute("mensagem", "Item atualizado com sucesso!");
			}
		} catch (DescontoInvalidoException ex) {
			attributes.addFlashAttribute("error", "Desconto inválido!");
		} catch (QuantidadeInvalidaException ex) {
			attributes.addFlashAttribute("error", "Quantidade inválida!");
		}
		return "redirect:/vendas/novo";
	}

}
