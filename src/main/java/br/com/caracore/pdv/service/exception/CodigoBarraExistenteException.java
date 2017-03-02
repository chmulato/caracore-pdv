package br.com.caracore.pdv.service.exception;

public class CodigoBarraExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CodigoBarraExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CodigoBarraExistenteException(String message) {
		super(message);
	}

}
