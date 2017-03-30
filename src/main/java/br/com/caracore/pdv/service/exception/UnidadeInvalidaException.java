package br.com.caracore.pdv.service.exception;

public class UnidadeInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnidadeInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnidadeInvalidaException(String message) {
		super(message);
	}

}
