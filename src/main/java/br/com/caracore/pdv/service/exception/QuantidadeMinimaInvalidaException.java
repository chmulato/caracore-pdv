package br.com.caracore.pdv.service.exception;

public class QuantidadeMinimaInvalidaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QuantidadeMinimaInvalidaException(String message, Throwable cause) {
		super(message, cause);
	}

	public QuantidadeMinimaInvalidaException(String message) {
		super(message);
	}

}
