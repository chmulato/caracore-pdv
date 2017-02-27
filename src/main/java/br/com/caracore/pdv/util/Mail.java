package br.com.caracore.pdv.util;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

@Component
public class Mail {
	
	private final String FROM_CVMULATO_COM_BR = "cvmulato@bol.com.br";

	@NotEmpty(message = "Login é obrigatório!")
	private String login;
	
	@NotEmpty(message = "E-mail é obrigatório!")
	private String to;
	
	private String subject;
	
	private String body;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getFROM_CVMULATO_COM_BR() {
		return FROM_CVMULATO_COM_BR;
	}

	
}
