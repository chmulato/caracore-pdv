package br.com.caracore.pdv.service.exception;

public class VendedorNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VendedorNaoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	public VendedorNaoEncontradoException(String message) {
		super(message);
	}

}
