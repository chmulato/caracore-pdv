package br.com.caracore.pdv.service.exception;

public class VendedorInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VendedorInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public VendedorInvalidoException(String message) {
		super(message);
	}

}
