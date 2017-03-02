package br.com.caracore.pdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.types.TipoOperador;
import br.com.caracore.pdv.repository.filter.OperadorFilter;
import br.com.caracore.pdv.service.OperadorService;
import br.com.caracore.pdv.service.exception.AdminExistenteException;
import br.com.caracore.pdv.service.exception.EmailInvalidoException;
import br.com.caracore.pdv.service.exception.LoginExistenteException;
import br.com.caracore.pdv.service.exception.SenhaInvalidaException;

@Controller
@RequestMapping("/operadores")
public class OperadoresController {

	@Autowired
	private OperadorService operadorService;
	
	@GetMapping
	public ModelAndView pesquisar(OperadorFilter filtroOperador) {
		ModelAndView mv = new ModelAndView("operador/pesquisa-operadores");
		if (filtroOperador != null) {
			mv.addObject("operadores", operadorService.pesquisar(filtroOperador));
		} else {
			filtroOperador = new OperadorFilter();
			filtroOperador.setNome("%");
		}
		return mv;		
	}
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Operador operador) {
		ModelAndView mv = new ModelAndView("operador/cadastro-operador");
		mv.addObject(operador);
		mv.addObject("tipos", TipoOperador.values());
		mv.addObject("lojas", operadorService.buscarLojas());
		return mv;
	}

	@PostMapping("/novo")
	public ModelAndView salvar(@Validated Operador operador, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(operador);
		}
		try {
			operadorService.salvar(operador);
			attributes.addFlashAttribute("mensagem", "Operador salvo com sucesso!");
			return new ModelAndView("redirect:/operadores/novo");
		} catch (AdminExistenteException ex) {
			errors.rejectValue("perfil", " ", ex.getMessage());
			return novo(operador);
		} catch (LoginExistenteException ex) {
			errors.rejectValue("nome", " ", ex.getMessage());
			return novo(operador);
		} catch (SenhaInvalidaException ex) {
			errors.rejectValue("repetirSenha", " ", ex.getMessage());
			return novo(operador);
		} catch (EmailInvalidoException ex) {
			errors.rejectValue("repetirEmail", " ", ex.getMessage());
			return novo(operador);
		}
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Operador operador = operadorService.pesquisarPorId(codigo);
		return novo(operador);
	}
	
	@DeleteMapping("/{codigo}")
	public String apagar(@PathVariable Long codigo, RedirectAttributes attributes) {
		operadorService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Operador removido com sucesso!");
		return "redirect:/operadores";
	}

}
