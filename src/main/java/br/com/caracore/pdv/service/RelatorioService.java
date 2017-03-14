package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;

@Service
public class RelatorioService {

	@Autowired
	private ItemVendaService itemVendaService;
	
	@Autowired
	private PagamentoService pagamentoService;
		
	/**
	 * Pesquisar o pagamento pelo código
	 * 
	 * @param codigoPagamento
	 * @return
	 */
	public Pagamento buscarPorCodigoPagamento(Long codigoPagamento) {
		return pagamentoService.pesquisarPorCodigo(codigoPagamento);
	}
	
	/**
	 * Método externo para buscar itens da compra
	 * 
	 * @param codigoPagamento
	 * @return
	 */
	public List<ItemVenda> buscarPorVenda(Venda venda) {
		return itemVendaService.buscarItens(venda);
	}

}
