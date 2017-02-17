package br.com.caracore.pdv.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.service.VendaService;
import br.com.caracore.pdv.util.Util;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private VendaService vendaService;
	
	@GetMapping("/produto")
	public ModelAndView pesquisarProduto(ProdutoFilter produtoFilter) {
		Long codigoProduto = null;
		String login = recuperarLogin();
		if (Util.validar(produtoFilter)) {
			if (Util.validar(produtoFilter.getCodigo())) {
				codigoProduto = Long.valueOf(produtoFilter.getCodigo());
			}
		}
		Venda venda = vendaService.cadastrar(codigoProduto, login);
		return novo(venda);
	}
	
	@GetMapping("/novo")
	public ModelAndView novo(Venda venda) {
		String login = null;
		List<Vendedor> listaVendedores = null;
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
		login = recuperarLogin();
		venda = vendaService.recuperarVendaEmAberto(login);
		listaVendedores = vendaService.listarVendedoresPorLogin(login);
		mv.addObject("vendedores", listaVendedores);
		mv.addObject(limparFiltro(venda));
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

	private String recuperarLogin() {
		String login = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			login = auth.getName(); 
		}
		return login;
	}
	
	private ProdutoFilter limparFiltro(Venda venda) {
		ProdutoFilter filtro = new ProdutoFilter("","");
		return filtro;
	}
}

