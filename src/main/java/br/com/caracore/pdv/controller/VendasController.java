package br.com.caracore.pdv.controller;

import java.util.List;

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

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.repository.filter.VendaFilter;
import br.com.caracore.pdv.repository.filter.VendedorFilter;
import br.com.caracore.pdv.service.VendaService;
import br.com.caracore.pdv.service.exception.ProdutoNaoCadastradoException;
import br.com.caracore.pdv.util.Util;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private VendaService vendaService;
	
	@GetMapping("/produto")
	public ModelAndView pesquisarProduto(ProdutoFilter produtoFilter, BindingResult result, RedirectAttributes attributes) {
		Long codigoProduto = null;
		Integer quantidade = null;
		String codigoBarra = null;
		Operador operador = recuperarOperador();
		if (Util.validar(produtoFilter)) {
			if (Util.validar(produtoFilter.getCodigo())) {
				codigoProduto = Long.valueOf(produtoFilter.getCodigo());
			}
			if (Util.validar(produtoFilter.getQuantidade())) {
				quantidade = produtoFilter.getQuantidade();
			}
			if (Util.validar(produtoFilter.getCodigoBarra())) {
				codigoBarra = produtoFilter.getCodigoBarra();
			}
		}
		Venda venda = null;
		try {
			venda = vendaService.comprar(codigoProduto, quantidade, codigoBarra, operador);
			return novo(venda);
		} catch (ProdutoNaoCadastradoException ex) {
			attributes.addFlashAttribute("error", ex.getMessage());
			return new ModelAndView("redirect:/vendas/novo");
		}
	}
	
	@GetMapping("/novo")
	public ModelAndView novo(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
		Operador operador = recuperarOperador();
		venda = buscarVendaEmAberto(venda, operador);
		mv.addObject("vendedores", buscarVendedores(operador));
		mv.addObject(limparFiltro(venda));
		mv.addObject(venda);
		return mv;
	}
	
	@GetMapping("/vendas")
	public ModelAndView pesquisarVendas(VendaFilter filtroVenda) {
		ModelAndView mv = new ModelAndView("venda/pesquisa-vendas");
		if (filtroVenda != null) {
			mv.addObject("vendas", vendaService.pesquisar(filtroVenda));
		} else {
			filtroVenda = new VendaFilter();
			filtroVenda.setVendedor("");
		}
		return mv;		
	}
	
	@PostMapping("/vendas")
	public ModelAndView pesquisarVendas(@Valid VendaFilter filtroVenda, BindingResult result, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("venda/pesquisa-vendas");
		if (filtroVenda != null) {
			mv.addObject("vendas", vendaService.pesquisar(filtroVenda));
		} else {
			filtroVenda = new VendaFilter();
			filtroVenda.setVendedor("");
		}
		return mv;		
	}
	
	@GetMapping("/vendedores")
	public ModelAndView pesquisar(VendedorFilter filtroVendedor) {
		ModelAndView mv = new ModelAndView("venda/seleciona-vendedor");
		if (filtroVendedor != null) {
			mv.addObject("vendedores", vendaService.pesquisar(filtroVendedor));
		} else {
			filtroVendedor = new VendedorFilter();
			filtroVendedor.setNome("%");
		}
		return mv;		
	}
	
	@GetMapping("/vendedor/{codigo}")
	public ModelAndView selecionar(@PathVariable Long codigo) {
		Venda venda = null;
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
		Vendedor vendedor = vendaService.selecionarPorId(codigo);
		if (Util.validar(vendedor)) {
			venda = vendaService.recuperarVendaEmAberto(vendedor);
			if (!Util.validar(venda)) {
				venda = new Venda();
			}
			venda.setVendedor(vendedor);
			mv.addObject(vendedor);
		}
		return novo(venda);
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

	/**
	 * Método interno para recuperar a venda
	 * 
	 * @param venda
	 * @param operador
	 * @return
	 */
	private Venda buscarVendaEmAberto(Venda venda, Operador operador) {
		Vendedor vendedor = null; 	
		if (!vendaService.validarVendaEmAndamento(venda)) {
			if (Util.validar(venda) && Util.validar(venda.getVendedor())) {
				vendedor = venda.getVendedor();
			} else {
				vendedor = vendaService.buscarVendedor(operador);
			}
			venda = vendaService.recuperarVendaEmAberto(vendedor);
		}
		return venda;
	}

	/**
	 * Método para recuperar operador logado
	 * 
	 * @return
	 */
	private Operador recuperarOperador() {
		Operador operador = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			String login = auth.getName();
			operador = vendaService.recuperarOperador(login);
		}
		return operador;
	}
	
	/**
	 * Método para auxiliar no filtro de tela de pesquisa de vendas em aberto
	 * 
	 * @param venda
	 * @return
	 */
	private ProdutoFilter limparFiltro(Venda venda) {
		String strUltimoCodigo = "";
		Integer quantidade = Integer.valueOf(1);
		String strUltimoCodigoBarra = "";
		String strUltimaDescricao = "";
		ItemVenda item = vendaService.recuperarUltimoItemVendaDaCesta(venda);
		if (Util.validar(item) && (Util.validar(item.getProduto()))) {
			strUltimoCodigo = item.getProduto().getCodigo().toString();
			if(Util.validar(item.getProduto().getCodigoBarra())) {
				strUltimoCodigoBarra = item.getProduto().getCodigoBarra().toString();
			}
			strUltimaDescricao = item.getProduto().getDescricao();
		}	
		ProdutoFilter filtro = new ProdutoFilter(strUltimoCodigo, quantidade, strUltimoCodigoBarra, strUltimaDescricao);
		return filtro;
	}
	
	/**
	 * Médoto para recuperar lista de vendedores da loja
	 * 
	 * @param operador
	 * @return
	 */
	private List<Vendedor> buscarVendedores(Operador operador) {
		List<Vendedor> vendedores = vendaService.listarVendedoresPorOperador(operador);
		if (!Util.validar(vendedores)) {
			vendedores = Util.criarListaDeVendedores();
		}
		return vendedores;
	}
	
}

