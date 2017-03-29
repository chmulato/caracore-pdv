package br.com.caracore.pdv.vo;

import org.springframework.stereotype.Component;

import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;

@Component
public class CompraVO {

	private Operador operador;
	
	private Vendedor vendedor;
	
	private Venda venda;

	public Operador getOperador() {
		return operador;
	}

	public void setOperador(Operador operador) {
		this.operador = operador;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

}
