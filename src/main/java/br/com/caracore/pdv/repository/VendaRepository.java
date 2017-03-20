package br.com.caracore.pdv.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.StatusVenda;

public interface VendaRepository extends JpaRepository<Venda, Long> {

	public List<Venda> findByData(Date data);
	
	public List<Venda> findByVendedor(Vendedor vendedor);
	
	public List<Venda> findByVendedorAndDataAndStatus(Vendedor vendedor, Date data, StatusVenda status);

	@Query("select v from Venda v where (v.data between ?1 and ?2) and v.vendedor = ?3 and v.status = ?4")
	public List<Venda> findByDataBetweenAndVendedorAndStatus(Date dataInicial, Date dataFinal, Vendedor vendedor, StatusVenda status);
	
	@Query("select v from Venda v where (v.data between ?1 and ?2) and v.vendedor.loja = ?3 and v.status = ?4")
	public List<Venda> findByDataBetweenAndLojaAndStatus(Date dataInicial, Date dataFinal, Loja loja, StatusVenda status);
	
	@Query("select v from Venda v where (v.data between ?1 and ?2) or v.vendedor = ?3 or v.vendedor.loja = ?4")
	public List<Venda> findByDataBetweenOrVendedorOrLoja(Date dataInicial, Date dataFinal, Vendedor vendedor, Loja loja);
	
	@Query("select v from Venda v where (v.data between ?1 and ?2) or (v.status <> ?3)")
	public List<Venda> findByDataBetweenOrStatus(Date dataInicial, Date dataFinal, StatusVenda status);
}