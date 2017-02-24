package br.com.caracore.pdv.service.exception;

public class GerenteExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GerenteExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public GerenteExistenteException(String message) {
		super(message);
	}


}
