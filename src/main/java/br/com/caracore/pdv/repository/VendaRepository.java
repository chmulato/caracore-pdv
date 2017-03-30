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

	public List<Venda> findByCodigoAndDataAndStatus(Long codigo, Date data, StatusVenda status);

	public List<Venda> findByVendedorAndDataAndStatus(Vendedor vendedor, Date data, StatusVenda status);

	@Query("select v from Venda v where (v.data between ?1 and ?2) and v.vendedor = ?3 and v.status = ?4")
	public List<Venda> findByDataBetweenAndVendedorAndStatus(Date dataInicial, Date dataFinal, Vendedor vendedor, StatusVenda status);

	@Query("select v from Venda v where (v.data between ?1 and ?2) and v.vendedor.loja = ?3 and v.status = ?4")
	public List<Venda> findByDataBetweenAndLojaAndStatus(Date dataInicial, Date dataFinal, Loja loja, StatusVenda status);

	@Query("select v from Venda v where (v.data between ?1 and ?2) and (v.vendedor = ?3) and (v.vendedor.loja = ?4) order by v.data desc")
	public List<Venda> findByDataBetweenAndVendedorAndLojaByOrderByDataDesc(Date dataInicial, Date dataFinal, Vendedor vendedor, Loja loja);

	@Query("select v from Venda v where (v.data between ?1 and ?2) and (v.vendedor = ?3) order by v.data desc")
	public List<Venda> findByDataBetweenAndVendedorByOrderByDataDesc(Date dataInicial, Date dataFinal, Vendedor vendedor);

	@Query("select v from Venda v where (v.data between ?1 and ?2) and (v.vendedor.loja = ?3) order by v.data desc")
	public List<Venda> findByDataBetweenAndLojaByOrderByDataDesc(Date dataInicial, Date dataFinal, Loja loja);

	@Query("select v from Venda v where (v.data between ?1 and ?2) order by v.data desc")
	public List<Venda> findByDataBetweenByOrderByDataDesc(Date dataInicial, Date dataFinal);
}