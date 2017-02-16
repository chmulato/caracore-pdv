package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.TipoVendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
	
	public List<Vendedor> findByNomeContainingIgnoreCase(String nome);

	public List<Vendedor> findByLoja(Loja loja);
	
    public Vendedor findByTipo(TipoVendedor tipo);

}