package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.EstoqueRepository;
import br.com.caracore.pdv.repository.ItemVendaRepository;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.QuantidadeInvalidaException;
import br.com.caracore.pdv.service.exception.QuantidadeNaoExistenteEmEstoqueException;
import br.com.caracore.pdv.util.Util;
import br.com.caracore.pdv.vo.CompraVO;

@Service
public class ItemVendaService {

	final private double ZERO = 0.0d;
	
	final private double PORCENTAGEM = 100.0d;

	final private int QUANTIDADE_MINIMA = 1;

	final private int QUANTIDADE_MAXIMA = 10000;

	@Autowired
	private EstoqueRepository estoqueRepository;

	@Autowired
	private ItemVendaRepository itemVendaRepository;

	@Autowired
	private SessionService sessionService;
	
	public ItemVenda pesquisarPorCodigo(Long codigo) {
		return itemVendaRepository.findOne(codigo);
	}

	public void atualizar(Long codigo, String desconto, String dinheiro, String quantidade) {
		if (Util.validar(codigo) && Util.validar(desconto)) {
			ItemVenda item = pesquisarPorCodigo(codigo);
			if (Util.validar(item)) {
				BigDecimal desc = BigDecimal.ZERO;
				try {
					desc = Util.formatarDesconto(desconto);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
				desc = descontoEmDinheiro(dinheiro, item, desc);
				item.setDesconto(desc);
			}
			if (Util.validar(quantidade)) {
				Integer quant = Integer.valueOf(0);
				try {
					quant = new Integer(quantidade);
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
				item.setQuantidade(quant);
			}
			salvar(item);
		}
	}
	
	/**
	 * Método interno para o desconto em dinheiro
	 * 
	 * @param dinheiro
	 * @param item
	 * @param desc
	 * @return
	 */
	private BigDecimal descontoEmDinheiro(String dinheiro, ItemVenda item, BigDecimal desc) {
		if (Util.validar(dinheiro)) {
			double valorTotal = (item.getQuantidade().intValue() * item.getPrecoUnitario().doubleValue());
			double valorDinheiro = desc.doubleValue();
			if (valorTotal >= valorDinheiro) {
				double valorDesconto = valorTotal - valorDinheiro;
				double descontoReal = PORCENTAGEM - ((valorDesconto / valorTotal) * PORCENTAGEM);
				desc = new BigDecimal(descontoReal);
			} else {
				desc = new BigDecimal(PORCENTAGEM);
			}
		}
		return desc;
	}
	
	/**
	 * Método interno para validar se a quantidade do produto se encontra em estoque 
	 * 
	 * @param item
	 */
	private void validarProdutoEmEstoque(ItemVenda item) {
		if ((Util.validar(item)) && (Util.validar(item.getCodigo())) && (Util.validar(item.getQuantidade()))) {
			long codigoItem = item.getCodigo().longValue();
			if (!Util.validar(item.getProduto())) {
				throw new QuantidadeNaoExistenteEmEstoqueException("Produto não existente em estoque!");
			} else {
				Loja loja = null;
				Integer quantidade = item.getQuantidade();
				Produto produto = item.getProduto();
				CompraVO session = sessionService.getSessionVO();
				if ((Util.validar(session))) {
					if (Util.validar(session.getVendedor())) {
						Vendedor vendedor = session.getVendedor();
						if (Util.validar(vendedor)) {
							loja = vendedor.getLoja();
						}
					}
					if (Util.validar(session.getVenda())) {
						Venda venda = session.getVenda();
						List<ItemVenda> itens = itemVendaRepository.findByVendaAndProduto(venda, produto);
						if (Util.validar(itens)) {
							int _quantidade = 0;
							for (ItemVenda itemVenda : itens) {
								long _codigoItem = itemVenda.getCodigo().longValue();
								if (codigoItem != _codigoItem) {
									if (Util.validar(itemVenda.getQuantidade())) {
										_quantidade = _quantidade + itemVenda.getQuantidade().intValue(); 
									}
								}
							}
							quantidade = Integer.valueOf(quantidade.intValue() + _quantidade);
						}
					}
				}
				Estoque estoque = estoqueRepository.findByLojaAndProduto(loja, produto);
				if (Util.validar(estoque)) {
					if (Util.validar(estoque.getQuantidade())) {
						int _quantidade = quantidade.intValue();
						int _quantidadeEstoque = estoque.getQuantidade().intValue();
						if (_quantidade > _quantidadeEstoque) {
							throw new QuantidadeNaoExistenteEmEstoqueException("Quantidade do produto não existente em estoque!");
						}
					} else {
						throw new QuantidadeInvalidaException("Quantidade inválida!");
					}
				} else {
					throw new QuantidadeNaoExistenteEmEstoqueException("Quantidade do produto não existente em estoque!");
				}
			}
		}
	}
	
	public void salvar(ItemVenda item) {
		validarDesconto(item);
		validarProdutoEmEstoque(item);
		itemVendaRepository.save(item);
	}

	/**
	 * Método interno para validar desconto
	 * 
	 * @param item
	 */
	private void validarDesconto(ItemVenda item) {
		if (Util.validar(item)) {
			if (Util.validar(item.getDesconto())) {
				double desconto = item.getDesconto().doubleValue();
				if ((desconto < ZERO) || (desconto > PORCENTAGEM)) {
					throw new DescontoInvalidoException("Desconto Inválido!");
				}
			}
			if (Util.validar(item.getQuantidade())) {
				int quantidade = item.getQuantidade().intValue();
				if ((quantidade < QUANTIDADE_MINIMA) || (quantidade > QUANTIDADE_MAXIMA)) {
					throw new QuantidadeInvalidaException("Quantidade Inválida!");
				}
			}
			calcularSubTotal(item);
		}
	}

	/**
	 * Método interno para calcular sub total da venda
	 * 
	 * @param item
	 */
	private void calcularSubTotal(ItemVenda item) {
		double subTotal = 0d;
		double preco = 0d;
		long quantidade = 0l;
		double desconto = 0d;
		if (Util.validar(item.getPrecoUnitario())) {
			preco = item.getPrecoUnitario().doubleValue();
		}
		if (Util.validar(item.getQuantidade())) {
			quantidade = item.getQuantidade().longValue();
		}
		if (Util.validar(item.getDesconto())) {
			desconto = item.getDesconto().doubleValue();
		}
		if (Util.validar(item.getSubTotal())) {
			subTotal = item.getSubTotal().doubleValue();
		}
		if (desconto >= ZERO && desconto <= PORCENTAGEM) {
			preco = preco * quantidade;
			subTotal = preco - (preco * (desconto/PORCENTAGEM));
		}
		item.setSubTotal(BigDecimal.valueOf(subTotal));
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
