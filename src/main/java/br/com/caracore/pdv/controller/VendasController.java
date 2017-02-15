package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.service.VendaService;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private VendaService vendaService;

	private String recuperarLogin() {
		String login = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			login = auth.getName(); 
		}
		return login;
	}
	
	private ProdutoFilter criarProdutoFilter(Venda venda) {
		ProdutoFilter filtro = new ProdutoFilter("","");
		return filtro;
	}
	
	@PostMapping("/produto/{codigo}")
	public ModelAndView pesquisarProduto(@PathVariable Long codigoProduto) {
		Venda venda = vendaService.cadastrar(codigoProduto, recuperarLogin());
		return novo(venda);
	}

	
	@GetMapping("/novo")
	public ModelAndView novo(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
		mv.addObject("vendedores", vendaService.listarVendedoresPorLogin(recuperarLogin()));
		mv.addObject(criarProdutoFilter(venda));
		mv.addObject(venda);
		
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Venda venda, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(venda);
		}
		vendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso!");
		return new ModelAndView("redirect:/vendas/novo");
	}

}

