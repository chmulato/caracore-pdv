package br.com.caracore.pdv.repository.filter;

public class ProdutoFilter {

	private String codigo;
	
	private String descricao;
	
	private String data;

	public ProdutoFilter() {
		super();
	}

	public ProdutoFilter(String codigo, String descricao, String data) {
		super();
		this.codigo = codigo;
		this.descricao = descricao;
		this.data = data;
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
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
