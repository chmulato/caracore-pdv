package br.com.caracore.pdv.repository.filter;

public class ProdutoFilter {

	private String codigo;
	
	private String codigoBarra;

	private String descricao;

	public ProdutoFilter() {
		super();
	}

	public ProdutoFilter(String codigo, String codigoBarra, String descricao) {
		super();
		this.codigo = codigo;
		this.codigoBarra = codigoBarra;
		this.descricao = descricao;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
