package br.com.caracore.pdv.vo;

import org.springframework.stereotype.Component;

@Component
public class SessionVO {

	private Long operadorId;
	
	private Long vendedorId;
	
	private Long vendaId;

	private Boolean autenticado;

	public Long getOperadorId() {
		return operadorId;
	}

	public void setOperadorId(Long operadorId) {
		this.operadorId = operadorId;
	}

	public Long getVendedorId() {
		return vendedorId;
	}

	public void setVendedorId(Long vendedorId) {
		this.vendedorId = vendedorId;
	}

	public Long getVendaId() {
		return vendaId;
	}

	public void setVendaId(Long vendaId) {
		this.vendaId = vendaId;
	}

	public Boolean getAutenticado() {
		return autenticado;
	}

	public void setAutenticado(Boolean autenticado) {
		this.autenticado = autenticado;
	}
	
}
