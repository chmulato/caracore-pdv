package br.com.caracore.pdv.service.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProdutoNaoEncontradoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProdutoNaoEncontradoException(String message) {
		super(message);
	}

}
