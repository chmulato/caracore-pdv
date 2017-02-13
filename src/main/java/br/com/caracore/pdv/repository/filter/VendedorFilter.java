package br.com.caracore.pdv.repository.filter;

import br.com.caracore.pdv.model.Loja;

public class VendedorFilter {

	private String nome;
	
	private Loja loja;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

}
