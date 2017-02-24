package br.com.caracore.pdv.service.exception;

public class CodigoExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CodigoExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodigoExistenteException(String message) {
		super(message);
	}

}
