package br.com.caracore.pdv.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.service.exception.EmailInvalidoException;
import br.com.caracore.pdv.service.exception.LoginInvalidoException;
import br.com.caracore.pdv.util.Mail;
import br.com.caracore.pdv.util.Util;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private UsuarioService usuarioService;

	public void send(Mail msg) {
		
		String login = "";
		String email = "";
		
		if (!Util.validar(msg)) {
			throw new EmailInvalidoException("Email Inválido!");
		}

		if (!Util.validar(msg.getLogin())) {
			throw new LoginInvalidoException("Login Inválido!");
		}
		
		if (!Util.validar(msg.getTo())) {
			throw new EmailInvalidoException("Email Inválido!");
		}		

		login = msg.getLogin();
		email = msg.getTo();

		Usuario usuario = usuarioService.buscar(login);
		if (!Util.validar(usuario)) {
			throw new LoginInvalidoException("Login Inválido!");
		}

		if (!usuario.getEmail().equals(email)) {
			throw new EmailInvalidoException("Email Inválido!");
		}
		
		msg.setSubject("Segue abaixo a senha do usuário: " + usuario.getNome() + ".");
		StringBuffer sb = new StringBuffer();
		sb.append("\r\n");
		sb.append(" --> Usuário:     " + usuario.getNome() + "\r\n");
		sb.append(" --> Sua senha é: " + usuario.getSenha()+ "\r\n");
		msg.setBody(sb.toString());
		
		if (Util.validar(msg.getSubject()) && Util.validar(msg.getBody())) {
			MimeMessage mail = javaMailSender.createMimeMessage();
			try {
				MimeMessageHelper helper = new MimeMessageHelper(mail, true);
				helper.setTo(msg.getTo());
				helper.setReplyTo(msg.getFROM_CVMULATO_COM_BR());
				helper.setFrom(msg.getFROM_CVMULATO_COM_BR());
				helper.setSubject(msg.getSubject());
				helper.setText(msg.getBody());
			} catch (MessagingException ex) {
				ex.printStackTrace();
				throw new EmailInvalidoException("Falha ao envial o e-mail!");
			} finally {
			}
			javaMailSender.send(mail);
		}
		
	}
}
