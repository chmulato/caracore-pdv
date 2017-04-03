package br.com.caracore.pdv.vo;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class EstoqueVO {

	private String produto;
	
	private BigDecimal valorUnitario;
	
	private Integer quantidade;

	private Integer estoqueMinimo;

	private Integer estoqueMaximo;

	private BigDecimal total;

	public EstoqueVO() {
		super();
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Integer estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public Integer getEstoqueMaximo() {
		return estoqueMaximo;
	}

	public void setEstoqueMaximo(Integer estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
