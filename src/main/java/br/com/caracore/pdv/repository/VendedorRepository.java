package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
	
	public List<Vendedor> findByNomeContainingIgnoreCase(String nome);

	public List<Vendedor> findByLoja(Loja loja);
	
    @Query(value = "SELECT * FROM VENDEDOR WHERE USUARIO_ID = ?1", nativeQuery = true)
    public Vendedor findByUsuarioId(Long usuarioId);
}