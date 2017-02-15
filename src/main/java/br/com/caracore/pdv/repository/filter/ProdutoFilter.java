package br.com.caracore.pdv.repository.filter;

public class ProdutoFilter {

	private String codigo;
	
	private String descricao;
	
	public ProdutoFilter() {
		super();
	}

	public ProdutoFilter(String codigo, String descricao) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
