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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.repository.filter.EstoqueFilter;
import br.com.caracore.pdv.service.EstoqueService;
import br.com.caracore.pdv.service.exception.LojaNaoCadastradaException;
import br.com.caracore.pdv.service.exception.ProdutoNaoCadastradoException;
import br.com.caracore.pdv.service.exception.QuantidadeInvalidaException;
import br.com.caracore.pdv.service.exception.QuantidadeMaximaInvalidaException;

@Controller
@RequestMapping("/estoques")
public class EstoquesController {
	
	@Autowired
	private EstoqueService estoqueService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Estoque estoque) {
		ModelAndView mv = new ModelAndView("estoque/cadastro-estoque");
		mv.addObject(estoque);
		mv.addObject("produtos", estoqueService.listaProdutos());
		mv.addObject("lojas", estoqueService.listaLojas());
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Estoque estoque, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(estoque);
		}
		try {
			estoqueService.salvar(estoque);
			attributes.addFlashAttribute("mensagem", "Estoque salva com sucesso!");
			return new ModelAndView("redirect:/estoques/novo");
		} catch (QuantidadeInvalidaException ex) {
			errors.rejectValue("quantidade", " ", ex.getMessage());
			errors.rejectValue("estoqueMinimo", " ", ex.getMessage());
			return novo(estoque);
		} catch (QuantidadeMaximaInvalidaException ex) {
			errors.rejectValue("quantidade", " ", ex.getMessage());
			errors.rejectValue("estoqueMinimo", " ", ex.getMessage());
			errors.rejectValue("estoqueMaximo", " ", ex.getMessage());
			return novo(estoque);
		} catch (LojaNaoCadastradaException ex) {
			errors.rejectValue("loja", " ", ex.getMessage());
			return novo(estoque);
		} catch (ProdutoNaoCadastradoException ex) {
			errors.rejectValue("produto", " ", ex.getMessage());
			return novo(estoque);
		}
	}
	
	@GetMapping
	public ModelAndView pesquisar(EstoqueFilter filtroEstoque) {
		ModelAndView mv = new ModelAndView("estoque/pesquisa-estoques");
		if (filtroEstoque != null) {
			mv.addObject("estoques", estoqueService.pesquisar(filtroEstoque));
		} else {
			filtroEstoque = new EstoqueFilter();
		}
		return mv;		
	}
	
	@GetMapping("{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Estoque estoque = estoqueService.pesquisarPorCodigo(codigo);
		return novo(estoque);
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		estoqueService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Estoque removido com sucesso!");
		return "redirect:/estoques";
	}

}
