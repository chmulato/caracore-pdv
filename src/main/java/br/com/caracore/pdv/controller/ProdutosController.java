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

import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.types.Unidade;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.service.ProdutoService;
import br.com.caracore.pdv.service.exception.CodigoBarraExistenteException;
import br.com.caracore.pdv.service.exception.CodigoExistenteException;
import br.com.caracore.pdv.service.exception.ProdutoExistenteException;
import br.com.caracore.pdv.service.exception.UnidadeInvalidaException;

@Controller
@RequestMapping("/produtos")
public class ProdutosController {
	
	private final int QUANTIDADE_UNITARIA = 1;
	
	@Autowired
	private ProdutoService produtoService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Produto produto) {
		ModelAndView mv = new ModelAndView("produto/cadastro-produto");
		mv.addObject("unidades", Unidade.values());
		mv.addObject(produto);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Produto produto, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(produto);
		}
		try {
			produtoService.salvar(produto);
			attributes.addFlashAttribute("mensagem", "Produto salvo com sucesso!");
			return new ModelAndView("redirect:/produtos/novo");
		} catch (UnidadeInvalidaException ex) {
			errors.rejectValue("unidade", " ", ex.getMessage());
			return novo(produto);
		} catch (CodigoExistenteException ex) {
			errors.rejectValue("codigo", " ", ex.getMessage());
			return novo(produto);
		} catch (CodigoBarraExistenteException ex) {
			errors.rejectValue("codigoBarra", " ", ex.getMessage());
			return novo(produto);
		} catch (ProdutoExistenteException ex) {
			errors.rejectValue("descricao", " ", ex.getMessage());
			return novo(produto);
		}
	}
	
	@GetMapping
	public ModelAndView pesquisar(ProdutoFilter filtroProduto) {
		ModelAndView mv = new ModelAndView("produto/pesquisa-produtos");
		if (filtroProduto != null) {
			mv.addObject("produtos", produtoService.pesquisar(filtroProduto));
			mv.addObject("produtoFilter", new ProdutoFilter("", Integer.valueOf(QUANTIDADE_UNITARIA), "", "", null, null));
		} else {
			filtroProduto = new ProdutoFilter();
			filtroProduto.setDescricao("%");
			filtroProduto.setCodigoBarra("%");
		}
		return mv;		
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Produto produto = produtoService.pesquisarPorCodigo(codigo);
		return novo(produto);
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		produtoService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Produto removido com sucesso!");
		return "redirect:/produtos";
	}

}

