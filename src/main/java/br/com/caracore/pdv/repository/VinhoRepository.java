package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Vinho;

public interface VinhoRepository extends JpaRepository<Vinho, Long> {
	
	public List<Vinho> findByNomeContainingIgnoreCase(String nome);

}
