package br.com.caracore.pdv.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Usuario;
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
		Usuario usuario = recuperarUsuario();
		if (Util.validar(produtoFilter)) {
			if (Util.validar(produtoFilter.getCodigo())) {
				codigoProduto = Long.valueOf(produtoFilter.getCodigo());
			}
		}
		Venda venda = vendaService.comprar(codigoProduto, usuario);
		return novo(venda);
	}
	
	@GetMapping("/novo")
	public ModelAndView novo(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
		Usuario usuario = recuperarUsuario();
		if (!vendaService.validarVendaEmAndamento(venda)) {
			Vendedor vendedor = vendaService.buscarVendedor(usuario);
			venda = vendaService.recuperarVendaEmAberto(vendedor);
		}
		mv.addObject("vendedores", buscarVendedores(usuario));
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
	
	@DeleteMapping(value="/{codigo}")
	public String excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		vendaService.excluirItemVenda(codigo);
		attributes.addFlashAttribute("mensagem", "Item excluído com sucesso!");
		return "redirect:/vendas/novo";
	}

	/**
	 * Método para recuperar usuário logado
	 * 
	 * @return
	 */
	private Usuario recuperarUsuario() {
		Usuario usuario = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			String login = auth.getName();
			usuario = vendaService.recuperarUsuario(login);
		}
		return usuario;
	}
	
	/**
	 * Método para auxiliar no filtro de tela de pesquisa de vendas em aberto
	 * 
	 * @param venda
	 * @return
	 */
	private ProdutoFilter limparFiltro(Venda venda) {
		ProdutoFilter filtro = new ProdutoFilter("","");
		return filtro;
	}
	
	/**
	 * Médoto para recuperar lista de vendedores da loja
	 * 
	 * @param usuario
	 * @return
	 */
	private List<Vendedor> buscarVendedores(Usuario usuario) {
		List<Vendedor> vendedores = vendaService.listarVendedoresPorUsuario(usuario);
		if (!Util.validar(vendedores)) {
			vendedores = Util.criarListaDeVendedores();
		}
		return vendedores;
	}
	
}

