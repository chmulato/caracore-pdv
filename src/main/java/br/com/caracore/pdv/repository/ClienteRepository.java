package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	public List<Cliente> findByNomeContainingIgnoreCase(String nome);

	public Cliente findByNomeIgnoreCase(String nome);

	public Cliente findByCpf(String cpf);
			
}
