package br.com.caracore.pdv.repository.filter;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Usuario;

public class VendedorFilter {

	private String nome;
	
	private Usuario usuario;

	private Loja loja;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

}
