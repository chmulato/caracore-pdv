package br.com.caracore.pdv.util;

import java.util.List;

public class CompraVO {
	
	private String cliente;
	
	private String cpf;
	
	private String vendedor;

	private String loja;

	private String subTotal;
	
	private String total;
	
	private String desconto;

	private String valorDesconto;

	private List<ItemVO> itens;
	
	public String getCliente() {
		return cliente;
	}

	public void setCliente(String cliente) {
		this.cliente = cliente;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getVendedor() {
		return vendedor;
	}

	public void setVendedor(String vendedor) {
		this.vendedor = vendedor;
	}

	public String getLoja() {
		return loja;
	}

	public void setLoja(String loja) {
		this.loja = loja;
	}

	public String getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(String subTotal) {
		this.subTotal = subTotal;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public String getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(String valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public List<ItemVO> getItens() {
		return itens;
	}

	public void setItens(List<ItemVO> itens) {
		this.itens = itens;
	}

}
