package br.com.caracore.pdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
	
	public Pagamento findByVenda(Venda venda);

}
