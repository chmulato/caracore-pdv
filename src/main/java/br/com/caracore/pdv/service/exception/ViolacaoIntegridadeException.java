package br.com.caracore.pdv.service.exception;

public class ViolacaoIntegridadeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ViolacaoIntegridadeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ViolacaoIntegridadeException(String message) {
		super(message);
	}

}
