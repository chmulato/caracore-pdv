package br.com.caracore.pdv.service.exception;

public class ProdutoExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProdutoExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProdutoExistenteException(String message) {
		super(message);
	}

}
