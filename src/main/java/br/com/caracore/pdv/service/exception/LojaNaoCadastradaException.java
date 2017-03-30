package br.com.caracore.pdv.service.exception;

public class LojaNaoCadastradaException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LojaNaoCadastradaException(String message, Throwable cause) {
		super(message, cause);
	}

	public LojaNaoCadastradaException(String message) {
		super(message);
	}

}
