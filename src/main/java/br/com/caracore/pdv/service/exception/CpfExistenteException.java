package br.com.caracore.pdv.service.exception;

public class CpfExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CpfExistenteException(String message, Throwable cause) {
		super(message, cause);
	}

	public CpfExistenteException(String message) {
		super(message);
	}

}
