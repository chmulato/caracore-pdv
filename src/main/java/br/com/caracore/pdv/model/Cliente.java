package br.com.caracore.pdv.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.com.caracore.pdv.util.Util;

@Entity
public class Cliente {

	final private String CLIENTE_NAO_INFORMADO = "NAO_INFORMADO";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	private Long cpf;
	
	@Size(max = 30, message = "Cliente não pode conter mais de 30 caracteres.")
	private String nome;

	@Transient
	private Long codigoPagamento;
	
	@Transient
	private String cpfFormatado;
	
	@Transient
	private boolean isDefault;
	
	public Cliente() {
		super();
	}

	public Cliente(String nome) {
		super();
		this.nome = nome;
	}
	
	public Cliente(Long cpf, String nome) {
		super();
		this.cpf = cpf;
		this.nome = nome;
	}

	public Cliente(String cpf, String nome) {
		super();
		Long _cpf = Long.valueOf(0l);
		try {
			if ((cpf != null) && (!cpf.equals(""))) {
				cpf = cpf.replace(".", "");
				_cpf = Long.valueOf(cpf);
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		this.cpf = _cpf;
		this.nome = nome;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCodigoPagamento() {
		return codigoPagamento;
	}

	public void setCodigoPagamento(Long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}

	public String getCpfFormatado() {
		if (cpf != null) {
			cpfFormatado = Util.formatString(String.valueOf(cpf), "###.###.###-##");
		}
		return cpfFormatado;
	}

	public void setCpfFormatado(String cpfFormatado) {
		this.cpfFormatado = cpfFormatado;
	}

	public Boolean getIsDefault() {
		Boolean result = Boolean.FALSE;
		if (Util.validar(nome)) {
			if (nome.equals(CLIENTE_NAO_INFORMADO)) {
				result = Boolean.TRUE;
			}
		}
		isDefault = result;
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
