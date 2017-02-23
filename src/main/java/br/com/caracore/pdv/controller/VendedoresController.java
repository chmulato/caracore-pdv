package br.com.caracore.pdv.controller;

import java.util.List;

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
import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.TipoVendedor;
import br.com.caracore.pdv.repository.filter.VendedorFilter;
import br.com.caracore.pdv.service.VendedorService;
import br.com.caracore.pdv.util.Util;

@Controller
@RequestMapping("/vendedores")
public class VendedoresController {
	
	@Autowired
	private VendedorService vendedorService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Vendedor vendedor) {
		ModelAndView mv = new ModelAndView("vendedor/cadastro-vendedor");
		mv.addObject(vendedor);
		mv.addObject("usuarios", buscarUsuarios());
		mv.addObject("tipos", TipoVendedor.values());
		mv.addObject("lojas", buscarLojas());
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Vendedor vendedor, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(vendedor);
		}
		vendedorService.salvar(vendedor);
		attributes.addFlashAttribute("mensagem", "Vendedor salvo com sucesso!");
		return new ModelAndView("redirect:/vendedores/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendedorFilter filtroVendedor) {
		ModelAndView mv = new ModelAndView("vendedor/pesquisa-vendedores");
		if (filtroVendedor != null) {
			mv.addObject("vendedores", vendedorService.pesquisar(filtroVendedor));
		} else {
			filtroVendedor = new VendedorFilter();
			filtroVendedor.setNome("%");
		}
		return mv;		
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Vendedor vendedor = vendedorService.pesquisarPorId(codigo);
		return novo(vendedor);
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		vendedorService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Vendedor removido com sucesso!");
		return "redirect:/vendedores";
	}
	
	private List<Usuario> buscarUsuarios() {
		List<Usuario> usuarios = vendedorService.buscarUsuarios();
		if (!Util.validar(usuarios)) {
			usuarios = Util.criarListaDeUsuarios();
		}
		return usuarios;
	}
	
	private List<Loja> buscarLojas() {
		List<Loja> lojas = vendedorService.buscarLojas();
		if (!Util.validar(lojas)) {
			lojas = Util.criarListaDeLojas();
		}
		return lojas;
	}

}
