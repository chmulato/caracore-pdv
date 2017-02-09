package br.com.caracore.pdv.model.types;

public enum ControlarEstoque {

	SIM("Sim"), NAO("Não");

	private String descricao;

	ControlarEstoque(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

}
