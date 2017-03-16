package br.com.caracore.pdv.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import br.com.caracore.pdv.util.Util;

@Entity
public class Cliente {

	@Transient
	final private String CLIENTE_NAO_INFORMADO = "NAO_INFORMADO";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	private String cpf;
	
	@Size(max = 30, message = "Cliente não pode conter mais de 30 caracteres.")
	private String nome;

	@Size(max = 30, message = "O e-mail não pode conter mais de 30 caracteres.")
	@Email
	private String email;

	@Transient
	private Long codigoPagamento;
	
	@Transient
	private String cpfFormatado;
	
	@Transient
	private boolean isDefault;

	@Transient
	private boolean thereIs;
	
	@Transient
	private boolean cpfVazio;
	
	public Cliente() {
		super();
	}

	public Cliente(String nome) {
		super();
		this.nome = nome;
	}
	
	public Cliente(String cpf, String nome, String email) {
		super();
		this.cpf = Util.removerFormatoCpf(cpf);
		this.nome = nome;
		this.email = email;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = Util.removerFormatoCpf(cpf);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getCodigoPagamento() {
		return codigoPagamento;
	}

	public void setCodigoPagamento(Long codigoPagamento) {
		this.codigoPagamento = codigoPagamento;
	}

	public String getCpfFormatado() {
		cpfFormatado = "";
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

	public boolean isThereIs() {
		return thereIs;
	}

	public void setThereIs(boolean thereIs) {
		this.thereIs = thereIs;
	}

	public boolean isCpfVazio() {
		boolean result = false;
		if ((cpf == null) || (cpf.equals(""))) {
			result = true;
		}
		cpfVazio = result;
		return cpfVazio;
	}

	public void setCpfVazio(boolean cpfVazio) {
		this.cpfVazio = cpfVazio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((codigoPagamento == null) ? 0 : codigoPagamento.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		if (codigoPagamento == null) {
			if (other.codigoPagamento != null)
				return false;
		} else if (!codigoPagamento.equals(other.codigoPagamento))
			return false;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
