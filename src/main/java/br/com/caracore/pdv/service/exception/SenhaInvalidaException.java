package br.com.caracore.pdv.service.exception;

public class SenhaInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SenhaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public SenhaInvalidaException(String message) {
		super(message);
	}

}
