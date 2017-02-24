package br.com.caracore.pdv.service.exception;

public class UsuarioImpostadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UsuarioImpostadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsuarioImpostadoException(String message) {
		super(message);
	}


}
