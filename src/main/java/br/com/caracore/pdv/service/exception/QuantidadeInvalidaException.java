package br.com.caracore.pdv.service.exception;

public class QuantidadeInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QuantidadeInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public QuantidadeInvalidaException(String message) {
		super(message);
	}

}
