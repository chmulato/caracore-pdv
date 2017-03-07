package br.com.caracore.pdv.service.exception;

public class CpfInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CpfInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public CpfInvalidoException(String message) {
		super(message);
	}

}
