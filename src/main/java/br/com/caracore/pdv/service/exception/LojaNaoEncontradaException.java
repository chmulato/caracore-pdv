package br.com.caracore.pdv.service.exception;

public class LojaNaoEncontradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LojaNaoEncontradaException(String message, Throwable cause) {
		super(message, cause);
	}

	public LojaNaoEncontradaException(String message) {
		super(message);
	}

}
