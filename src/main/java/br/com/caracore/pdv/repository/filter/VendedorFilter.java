package br.com.caracore.pdv.repository.filter;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Operador;

public class VendedorFilter {

	private String nome;
	
	private Operador operador;

	private Loja loja;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

}
