package br.com.caracore.pdv.service.exception;

public class LoginExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public LoginExistenteException(String message) {
		super(message);
	}

}
