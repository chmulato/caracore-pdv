package br.com.caracore.pdv.service.exception;

public class AdminExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AdminExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public AdminExistenteException(String message) {
		super(message);
	}

}
