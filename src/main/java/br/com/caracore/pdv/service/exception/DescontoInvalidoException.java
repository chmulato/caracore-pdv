package br.com.caracore.pdv.service.exception;

public class DescontoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DescontoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DescontoInvalidoException(String message) {
		super(message);
	}

}
