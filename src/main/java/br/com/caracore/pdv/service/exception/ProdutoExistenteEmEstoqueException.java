package br.com.caracore.pdv.service.exception;

public class ProdutoExistenteEmEstoqueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProdutoExistenteEmEstoqueException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProdutoExistenteEmEstoqueException(String message) {
		super(message);
	}

}
