package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.repository.filter.LojaFilter;
import br.com.caracore.pdv.service.LojaService;

@Controller
@RequestMapping("/lojas")
public class LojasController {
	
	@Autowired
	private LojaService lojaService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Loja loja) {
		ModelAndView mv = new ModelAndView("loja/cadastro-loja");
		mv.addObject(loja);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Loja loja, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(loja);
		}
		lojaService.salvar(loja);
		attributes.addFlashAttribute("mensagem", "Loja salva com sucesso!");
		return new ModelAndView("redirect:/lojas/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(LojaFilter filtroLoja) {
		ModelAndView mv = new ModelAndView("loja/pesquisa-lojas");
		if (filtroLoja != null) {
			mv.addObject("lojas", lojaService.pesquisar(filtroLoja));
		} else {
			filtroLoja = new LojaFilter();
			filtroLoja.setNome("%");
		}
		return mv;		
	}
	
	@GetMapping("{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Loja loja = lojaService.pesquisarPorCodigo(codigo);
		return novo(loja);
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		lojaService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Loja removida com sucesso!");
		return "redirect:/lojas";
	}

}
