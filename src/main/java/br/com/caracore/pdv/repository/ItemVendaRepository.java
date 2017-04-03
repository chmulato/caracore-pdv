package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Venda;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
	
	List<ItemVenda> findByVendaAndProduto(Venda venda, Produto produto); 
	
	List<ItemVenda> findByVenda(Venda venda);

}
