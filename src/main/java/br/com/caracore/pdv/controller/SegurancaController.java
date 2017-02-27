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

import br.com.caracore.pdv.service.EmailService;
import br.com.caracore.pdv.service.exception.EmailInvalidoException;
import br.com.caracore.pdv.service.exception.LoginInvalidoException;
import br.com.caracore.pdv.util.Mail;

@Controller
public class SegurancaController {
	
	@Autowired
	public EmailService emailService;

	
	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user) {
		if (user != null) {
			return "redirect:/vendas/novo";
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
