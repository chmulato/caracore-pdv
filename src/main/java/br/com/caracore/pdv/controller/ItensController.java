package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.service.ItemVendaService;

@Controller
@RequestMapping("/itens")
public class ItensController {

	@Autowired
	private ItemVendaService itemVendaService;
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid ItemVenda item, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(item);
		}
		itemVendaService.salvar(item);
		attributes.addFlashAttribute("mensagem", "Item salvo com sucesso!");
		return new ModelAndView("redirect:/vendas/novo");
	}

	@GetMapping("/novo")
	public ModelAndView novo(ItemVenda item) {
		ModelAndView mv = new ModelAndView("item/cadastro-item");
		mv.addObject(item);
		return mv;
	}

	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		ItemVenda item = itemVendaService.pesquisarPorId(codigo);
		return novo(item);
	}
}
