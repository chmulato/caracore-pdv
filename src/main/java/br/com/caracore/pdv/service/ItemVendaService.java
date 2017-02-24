package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.repository.ItemVendaRepository;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.QuantidadeInvalidaException;
import br.com.caracore.pdv.util.Util;

@Service
public class ItemVendaService {

	final private double ZERO = 0.0d;
	
	final private double PORCENTAGEM = 100.0d;

	final private int QUANTIDADE_MAXIMA = 10000;

	@Autowired
	private ItemVendaRepository itemVendaRepository;

	public ItemVenda pesquisarPorCodigo(Long codigo) {
		return itemVendaRepository.findOne(codigo);
	}

	public void salvar(ItemVenda itemVenda) {
		if (Util.validar(itemVenda)) {
			if (Util.validar(itemVenda.getDesconto())) {
				double desconto = itemVenda.getDesconto().doubleValue();
				if ((desconto < ZERO) || (desconto > PORCENTAGEM)) {
					throw new DescontoInvalidoException("Desconto Inválido!");
				}
			}
			if (Util.validar(itemVenda.getQuantidade())) {
				int quantidade = itemVenda.getQuantidade().intValue();
				if (quantidade > QUANTIDADE_MAXIMA) {
					throw new QuantidadeInvalidaException("Quantidade Inválida!");
				}
			}
			calcularSubTotal(itemVenda);
		}
		itemVendaRepository.save(itemVenda);
	}

	private void calcularSubTotal(ItemVenda itemVenda) {
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
				salvar(itemVenda);
			}
		}
		return itens;
	}

}