package br.com.caracore.pdv.service.exception;

public class VendaNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VendaNaoEncontradaException(String message, Throwable cause) {
		super(message, cause);
	}

	public VendaNaoEncontradaException(String message) {
		super(message);
	}

}
