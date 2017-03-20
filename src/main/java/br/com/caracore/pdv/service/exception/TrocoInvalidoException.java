package br.com.caracore.pdv.service.exception;

public class TrocoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TrocoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TrocoInvalidoException(String message) {
		super(message);
	}

}
