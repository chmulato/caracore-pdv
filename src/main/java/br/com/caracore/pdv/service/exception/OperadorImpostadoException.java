package br.com.caracore.pdv.service.exception;

public class OperadorImpostadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public OperadorImpostadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public OperadorImpostadoException(String message) {
		super(message);
	}

}
