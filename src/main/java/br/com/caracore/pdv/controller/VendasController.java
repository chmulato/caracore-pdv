package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.filter.VendaFilter;
import br.com.caracore.pdv.repository.filter.VendedorFilter;
import br.com.caracore.pdv.service.VendaService;
import br.com.caracore.pdv.service.VendedorService;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private VendaService vendaService;

	@Autowired
	private VendedorService vendedorService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/cadastro-venda");
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
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter filtroVenda) {
		ModelAndView mv = new ModelAndView("venda/pesquisa-vendas");
		if (filtroVenda != null) {
			mv.addObject("vendas", vendaService.pesquisar(filtroVenda));
			mv.addObject("vendedores", vendedorService.pesquisar(transformarFiltro(filtroVenda)));
		}
		return mv;		
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Venda venda = vendaService.pesquisarPorId(codigo);
		return novo(venda);
	}
	
	@DeleteMapping("/{codigo}")
	public String apagar(@PathVariable Long codigo, RedirectAttributes attributes) {
		vendaService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Venda removida com sucesso!");
		return "redirect:/vendas";
	}
	
	private VendedorFilter transformarFiltro(VendaFilter vendaFiltro) {
		VendedorFilter vendedorFiltro = null;
		if (vendaFiltro.getVendedor() != null) {
			if (vendaFiltro.getVendedor().getNome() != null) {
				Vendedor vendedor = vendaFiltro.getVendedor();
				vendedorFiltro = new VendedorFilter();
				vendedorFiltro.setNome(vendedor.getNome());
			}
		}
		return vendedorFiltro;
	}
}

