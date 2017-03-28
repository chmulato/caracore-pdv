package br.com.caracore.pdv.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.service.EmailService;
import br.com.caracore.pdv.service.OperadorService;
import br.com.caracore.pdv.service.exception.EmailInvalidoException;
import br.com.caracore.pdv.service.exception.LoginInvalidoException;
import br.com.caracore.pdv.util.Mail;
import br.com.caracore.pdv.util.Util;

@Controller
public class SegurancaController {
	
	@Autowired
	public EmailService emailService;

	@Autowired
	public OperadorService operadorService;
	
	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user) {
		if ((Util.validar(user)) && (Util.validar(user.getUsername()))) {
			String nome = user.getUsername();
			Operador operador = operadorService.buscar(nome);
			if (Util.validar(operador)) {
				operadorService.setAutenticado(operador);
				return "redirect:/vendas/vendedores";
			}
		}
		return "login";
	}

	@RequestMapping("/esqueceu-a-senha")
	public ModelAndView esqueceuSenha() {
		ModelAndView mv = new ModelAndView("esqueceu-a-senha");
		Mail mail = new Mail();
		mail.setLogin("");
		mail.setTo("");
		mv.addObject(mail);
		return mv;
	}

	@GetMapping("/recupera-senha")
	public ModelAndView novo(Mail mail) {
		ModelAndView mv = new ModelAndView("esqueceu-a-senha");
		mail = emailService.validarMail(mail);
		mv.addObject(mail);
		return mv;
	}

	@PostMapping("/recupera-senha")
	public ModelAndView salvar(@Validated Mail mail, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(mail);
		}
		try {
			emailService.send(mail);
			attributes.addFlashAttribute("mensagem", "E-mail enviado com sucesso!");
			return new ModelAndView("redirect:/esqueceu-a-senha");
		} catch (LoginInvalidoException ex) {
			errors.rejectValue("login", " ", ex.getMessage());
			return novo(mail);
		} catch (EmailInvalidoException ex) {
			errors.rejectValue("to", " ", ex.getMessage());
			return novo(mail);
		}
	}
	
}
