package br.com.caracore.pdv.service.exception;

public class LoginInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginInvalidoException(String message) {
		super(message);
	}

}
