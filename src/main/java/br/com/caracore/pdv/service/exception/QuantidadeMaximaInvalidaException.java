package br.com.caracore.pdv.service.exception;

public class QuantidadeMaximaInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QuantidadeMaximaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public QuantidadeMaximaInvalidaException(String message) {
		super(message);
	}

}
