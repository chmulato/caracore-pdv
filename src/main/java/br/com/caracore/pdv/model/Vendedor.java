package br.com.caracore.pdv.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import br.com.caracore.pdv.model.types.TipoVendedor;

@Entity
public class Vendedor {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotBlank
	private String nome;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoVendedor tipo;

}
