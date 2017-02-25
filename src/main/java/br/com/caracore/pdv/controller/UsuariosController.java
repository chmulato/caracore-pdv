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

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.types.TipoUsuario;
import br.com.caracore.pdv.repository.filter.UsuarioFilter;
import br.com.caracore.pdv.service.UsuarioService;
import br.com.caracore.pdv.service.exception.AdminExistenteException;
import br.com.caracore.pdv.service.exception.EmailInvalidoException;
import br.com.caracore.pdv.service.exception.LoginExistenteException;
import br.com.caracore.pdv.service.exception.SenhaInvalidaException;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter filtroUsuario) {
		ModelAndView mv = new ModelAndView("usuario/pesquisa-usuarios");
		if (filtroUsuario != null) {
			mv.addObject("usuarios", usuarioService.pesquisar(filtroUsuario));
		} else {
			filtroUsuario = new UsuarioFilter();
			filtroUsuario.setNome("%");
		}
		return mv;		
	}
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("usuario/cadastro-usuario");
		mv.addObject(usuario);
		mv.addObject("tipos", TipoUsuario.values());
		return mv;
	}

	@PostMapping("/novo")
	public ModelAndView salvar(@Validated Usuario usuario, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(usuario);
		}
		try {
			usuarioService.salvar(usuario);
			attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
			return new ModelAndView("redirect:/usuarios/novo");
		} catch (AdminExistenteException ex) {
			errors.rejectValue("perfil", " ", ex.getMessage());
			return novo(usuario);
		} catch (LoginExistenteException ex) {
			errors.rejectValue("nome", " ", ex.getMessage());
			return novo(usuario);
		} catch (SenhaInvalidaException ex) {
			errors.rejectValue("repetirSenha", " ", ex.getMessage());
			return novo(usuario);
		} catch (EmailInvalidoException ex) {
			errors.rejectValue("repetirEmail", " ", ex.getMessage());
			return novo(usuario);
		}
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Usuario usuario = usuarioService.pesquisarPorId(codigo);
		return novo(usuario);
	}
	
	@DeleteMapping("/{codigo}")
	public String apagar(@PathVariable Long codigo, RedirectAttributes attributes) {
		usuarioService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Usuário removido com sucesso!");
		return "redirect:/usuarios";
	}

}
