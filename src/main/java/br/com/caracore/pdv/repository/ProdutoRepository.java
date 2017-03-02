package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	public Produto findByCodigoBarra(String codigoBarra);

	public Produto findByDescricaoIgnoreCase(String descricao);

	public Produto findByCodigoBarraIgnoreCase(String codigoBarra);

	public List<Produto> findByDescricaoContainingIgnoreCase(String descricao);

	public List<Produto> findByCodigoBarraContainingIgnoreCase(String codigoBarra);
	
}