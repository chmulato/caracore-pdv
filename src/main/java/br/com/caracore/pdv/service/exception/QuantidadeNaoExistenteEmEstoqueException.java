package br.com.caracore.pdv.service.exception;

public class QuantidadeNaoExistenteEmEstoqueException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QuantidadeNaoExistenteEmEstoqueException(String message, Throwable cause) {
		super(message, cause);
	}

	public QuantidadeNaoExistenteEmEstoqueException(String message) {
		super(message);
	}

}
