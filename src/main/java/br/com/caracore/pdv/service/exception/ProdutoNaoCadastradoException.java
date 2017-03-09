package br.com.caracore.pdv.service.exception;

public class ProdutoNaoCadastradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ProdutoNaoCadastradoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProdutoNaoCadastradoException(String message) {
		super(message);
	}

}
