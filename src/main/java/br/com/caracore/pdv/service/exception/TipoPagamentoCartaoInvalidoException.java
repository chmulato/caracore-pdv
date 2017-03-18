package br.com.caracore.pdv.service.exception;

public class TipoPagamentoCartaoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public TipoPagamentoCartaoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public TipoPagamentoCartaoInvalidoException(String message) {
		super(message);
	}

}
