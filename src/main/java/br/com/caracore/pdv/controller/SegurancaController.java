package br.com.caracore.pdv.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SegurancaController {
	
	@RequestMapping("/login")
	public String login(@AuthenticationPrincipal User user) {
		if (user != null) {
			return "redirect:/vinhos";
		}
		return "login";
	}

	@RequestMapping("/esqueceu-a-senha")
	public String esqueceuSenha() {
		return "esqueceu-a-senha";
	}
	
}
