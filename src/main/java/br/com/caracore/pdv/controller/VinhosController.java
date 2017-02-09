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

import br.com.caracore.pdv.model.Vinho;
import br.com.caracore.pdv.model.types.TipoVinho;
import br.com.caracore.pdv.repository.filter.VinhoFilter;
import br.com.caracore.pdv.service.VinhoService;

@Controller
@RequestMapping("/vinhos")
public class VinhosController {
	
	@Autowired
	private VinhoService vinhoService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Vinho vinho) {
		ModelAndView mv = new ModelAndView("vinho/cadastro-vinho");
		mv.addObject(vinho);
		mv.addObject("tipos", TipoVinho.values());
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Vinho vinho, BindingResult result,
			RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(vinho);
		}
		vinhoService.salvar(vinho);
		attributes.addFlashAttribute("mensagem", "Vinho salvo com sucesso!");
		return new ModelAndView("redirect:/vinhos/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(VinhoFilter filtroVinho) {
		ModelAndView mv = new ModelAndView("vinho/pesquisa-vinhos");
		if (filtroVinho != null) {
			mv.addObject("vinhos", vinhoService.pesquisar(filtroVinho));
		} else {
			filtroVinho = new VinhoFilter();
			filtroVinho.setNome("%");
		}
		return mv;		
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Vinho vinho = vinhoService.pesquisarPorId(codigo);
		return novo(vinho);
	}
	
	@DeleteMapping("/{codigo}")
	public String apagar(@PathVariable Long codigo, RedirectAttributes attributes) {
		vinhoService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Vinho removido com sucesso!");
		return "redirect:/vinhos";
	}

}
