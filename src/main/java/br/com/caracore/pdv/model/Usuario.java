package br.com.caracore.pdv.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

import br.com.caracore.pdv.model.types.TipoUsuario;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotEmpty(message = "Usuário é obrigatório!")
	@Size(max = 15, message = "A usuário não pode conter mais de 15 caracteres.")
	private String nome;
	
	@NotEmpty(message = "Senha é obrigatória!")
	@Size(max = 15, message = "A senha não pode conter mais de 15 caracteres.")
	private String senha;
	
	@NotEmpty(message = "Repetir senha é obrigatória!")
	@Size(max = 15, message = "Repetir senha não pode conter mais de 15 caracteres.")
	@Transient
	private String repetirSenha;
	
	@NotNull(message = "Perfil é obrigatório!")
	@Enumerated(EnumType.STRING)
	private TipoUsuario perfil;

	@NotNull(message = "Loja é obrigatória!")
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LOJA_ID")
	private Loja loja;

	@NotEmpty(message = "E-mail é obrigatório!")
	@Size(max = 30, message = "O e-mail não pode conter mais de 30 caracteres.")
	private String email;

	@NotEmpty(message = "Repetir e-mail é obrigatório!")
	@Size(max = 30, message = "Repetir e-mail não pode conter mais de 30 caracteres.")
	@Transient
	private String repetirEmail;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getRepetirSenha() {
		return repetirSenha;
	}

	public void setRepetirSenha(String repetirSenha) {
		this.repetirSenha = repetirSenha;
	}

	public TipoUsuario getPerfil() {
		return perfil;
	}

	public void setPerfil(TipoUsuario perfil) {
		this.perfil = perfil;
	}

	public Loja getLoja() {
		return loja;
	}

	public void setLoja(Loja loja) {
		this.loja = loja;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRepetirEmail() {
		return repetirEmail;
	}

	public void setRepetirEmail(String repetirEmail) {
		this.repetirEmail = repetirEmail;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((loja == null) ? 0 : loja.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((perfil == null) ? 0 : perfil.hashCode());
		result = prime * result + ((repetirEmail == null) ? 0 : repetirEmail.hashCode());
		result = prime * result + ((repetirSenha == null) ? 0 : repetirSenha.hashCode());
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
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
		Usuario other = (Usuario) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (loja == null) {
			if (other.loja != null)
				return false;
		} else if (!loja.equals(other.loja))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (perfil == null) {
			if (other.perfil != null)
				return false;
		} else if (!perfil.equals(other.perfil))
			return false;
		if (repetirEmail == null) {
			if (other.repetirEmail != null)
				return false;
		} else if (!repetirEmail.equals(other.repetirEmail))
			return false;
		if (repetirSenha == null) {
			if (other.repetirSenha != null)
				return false;
		} else if (!repetirSenha.equals(other.repetirSenha))
			return false;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		return true;
	}

}
