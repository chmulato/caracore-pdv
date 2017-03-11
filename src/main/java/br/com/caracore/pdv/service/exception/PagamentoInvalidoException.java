package br.com.caracore.pdv.service.exception;

public class PagamentoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PagamentoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public PagamentoInvalidoException(String message) {
		super(message);
	}

}
