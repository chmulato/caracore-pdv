package br.com.caracore.pdv.repository.filter;

import java.util.Date;

import br.com.caracore.pdv.model.Vendedor;

public class VendaFilter {
	
	private Long codigo;
	
	private Date data;
	
	private Vendedor vendedor;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

}
