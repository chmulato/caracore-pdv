package br.com.caracore.pdv.service.exception;

public class NomeExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NomeExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public NomeExistenteException(String message) {
		super(message);
	}

}
