package br.com.caracore.pdv.model.types;

public enum StatusCliente {

	LIBERAR("OK"), RESTRINGIR("Restrito"), BLOQUEAR("Bloqueado");

	private String descricao;

	StatusCliente(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
