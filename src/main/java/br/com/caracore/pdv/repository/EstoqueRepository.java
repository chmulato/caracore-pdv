package br.com.caracore.pdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Estoque;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

}