package br.com.caracore.pdv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Produto;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

	List<Estoque> findByLoja(Loja loja);

	List<Estoque> findByProduto(Produto produto);

	List<Estoque> findByLojaAndProduto(Loja loja, Produto produto);

	@Query("select e from Estoque e where (e.data between ?1 and ?2) and e.loja = ?3 and e.produto = ?4 order by e.data desc")
	public List<Estoque> findByDataBetweenAndLojaAndProdutoByOrderByDataDesc(Date dataInicial, Date dataFinal, Loja loja, Produto produto);
	
	@Query("select e from Estoque e where (e.data between ?1 and ?2) and e.loja = ?3 order by e.data desc")
	public List<Estoque> findByDataBetweenAndLojaByOrderByDataDesc(Date dataInicial, Date dataFinal, Loja loja);
	
	@Query("select e from Estoque e where (e.data between ?1 and ?2) and e.produto = ?3 order by e.data desc")
	public List<Estoque> findByDataBetweenAndProdutoByOrderByDataDesc(Date dataInicial, Date dataFinal, Produto produto);
	
	@Query("select e from Estoque e where (e.data between ?1 and ?2) order by e.data desc")
	public List<Estoque> findByDataBetweenByOrderByDataDesc(Date dataInicial, Date dataFinal);
	
}