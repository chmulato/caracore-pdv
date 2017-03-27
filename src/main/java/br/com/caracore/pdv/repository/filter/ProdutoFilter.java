package br.com.caracore.pdv.repository.filter;

public class ProdutoFilter {

	private String codigo;

	private Integer quantidade;
	
	private String codigoBarra;

	private String descricao;

	private Long codigoVenda;

	private Long codigoVendedor;
	
	public ProdutoFilter() {
		super();
	}

	public ProdutoFilter(String codigo, Integer quantidade, String codigoBarra, String descricao, Long codigoVenda, Long codigoVendedor) {
		super();
		this.codigo = codigo;
		this.quantidade = quantidade;
		this.codigoBarra = codigoBarra;
		this.descricao = descricao;
		this.codigoVenda = codigoVenda;
		this.codigoVendedor = codigoVendedor;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
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

	public Long getCodigoVenda() {
		return codigoVenda;
	}

	public void setCodigoVenda(Long codigoVenda) {
		this.codigoVenda = codigoVenda;
	}

	public Long getCodigoVendedor() {
		return codigoVendedor;
	}

	public void setCodigoVendedor(Long codigoVendedor) {
		this.codigoVendedor = codigoVendedor;
	}
	
}
