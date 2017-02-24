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

	final private double ZERO = 0.0d;
	
	final private double PORCENTAGEM = 100.0d;

	@Autowired
	private ItemVendaRepository itemVendaRepository;

	public ItemVenda pesquisarPorCodigo(Long codigo) {
		return itemVendaRepository.findOne(codigo);
	}

	public void salvar(ItemVenda itemVenda) {
		if (Util.validar(itemVenda)) {
			double subTotal = 0d;
			double preco = 0d;
			long quantidade = 0l;
			double desconto = 0d;
			if (Util.validar(itemVenda.getPrecoUnitario())) {
				preco = itemVenda.getPrecoUnitario().doubleValue();
			}
			if (Util.validar(itemVenda.getQuantidade())) {
				quantidade = itemVenda.getQuantidade().longValue();
			}
			if (Util.validar(itemVenda.getDesconto())) {
				desconto = itemVenda.getDesconto().doubleValue();
			}
			if (Util.validar(itemVenda.getSubTotal())) {
				subTotal = itemVenda.getSubTotal().doubleValue();
			}
			if (desconto >= ZERO && desconto <= PORCENTAGEM) {
				preco = preco * quantidade;
				subTotal = preco - (preco * (desconto/PORCENTAGEM));
			}
			itemVenda.setSubTotal(BigDecimal.valueOf(subTotal));
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
