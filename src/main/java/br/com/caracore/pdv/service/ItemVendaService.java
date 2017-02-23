package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.repository.ItemVendaRepository;
import br.com.caracore.pdv.util.Util;

@Service
public class ItemVendaService {

	@Autowired
	private ItemVendaRepository itemVendaRepository;

	public ItemVenda pesquisarPorCodigo(Long codigo) {
		return itemVendaRepository.findOne(codigo);
	}

	public void salvar(ItemVenda itemVenda) {
		if (Util.validar(itemVenda)) {
			long lngSubTotal = 0l;
			long lngPreco = 0l;
			long lngQtd = 0l;
			long lngDesconto = 0l;
			if (Util.validar(itemVenda.getPrecoUnitario())) {
				lngPreco = itemVenda.getPrecoUnitario().longValue();
			}
			if (Util.validar(itemVenda.getQuantidade())) {
				lngQtd = itemVenda.getQuantidade().longValue();
			}
			if (Util.validar(itemVenda.getDesconto())) {
				lngDesconto = itemVenda.getDesconto().longValue();
			}
			if (Util.validar(itemVenda.getSubTotal())) {
				lngSubTotal = itemVenda.getSubTotal().longValue();
			}
			if (lngDesconto >= 0 && lngDesconto <= 1) {
				lngPreco = lngPreco * lngQtd;
				lngSubTotal = lngPreco - (lngPreco * lngDesconto);
			}
			itemVenda.setSubTotal(BigDecimal.valueOf(lngSubTotal));
		}
		itemVendaRepository.save(itemVenda);
	}
	
	public void excluir(Long codigo) {
		if (Util.validar(codigo)) {
			itemVendaRepository.delete(codigo);
		}
	}

	List<ItemVenda> buscarItens(Venda venda) {
		List<ItemVenda> itens = null;
		if (Util.validar(venda)) {
			itens = itemVendaRepository.findByVenda(venda);
		}
		return itens;
	}
	
	public List<ItemVenda> salvarItens(List<ItemVenda> itens, Venda venda) {
		if (Util.validar(itens)) {
			for (ItemVenda itemVenda : itens) {
				itemVenda.setVenda(venda);
				itemVendaRepository.save(itemVenda);
			}
		}
		return itens;
	}

}
