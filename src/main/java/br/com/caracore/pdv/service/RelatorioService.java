package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.TipoPagamentoCartao;
import br.com.caracore.pdv.util.Util;
import br.com.caracore.pdv.vo.VendaDiariaVO;

@Service
public class RelatorioService {

	final private int TIPO_ZERO_PAGAMENTO_DEBITO = 0;

	final private int TIPO_UM_PAGAMENTO_CREDITO = 1;
	
	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ItemVendaService itemVendaService;

	@Autowired
	private LojaService lojaService;
	
	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private VendaService vendaService;

	@Autowired
	private VendedorService vendedorService;
	
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
		List<ItemVenda> lista = itemVendaService.buscarItens(venda);
		if (Util.validar(lista)) {
			List<ItemVenda> result = new ArrayList<>();
			for (ItemVenda itemVenda : lista) {
				if (Util.validar(itemVenda.getProduto())) {
					if (Util.validar(itemVenda.getProduto().getDescricao())) {
						String descricao = itemVenda.getProduto().getDescricao();
						itemVenda.setDescricaoProduto(descricao);
					}
				}
				result.add(itemVenda);
			}
			lista = result;
		}
		return lista;
	}

	/**
	 * Método externo para recuperar nome do cliente
	 * 
	 * @param venda
	 * @return
	 */
	public String cliente(Venda venda, String cpf) {
		String nome = "";
		if (Util.validar(venda.getCliente())) {
			nome = venda.getCliente().getNome(); 
		} else {
			if (Util.validar(cpf)) {
				Cliente cliente = clienteService.pesquisarPorCpf(cpf);
				if (Util.validar(cliente)) {
					nome = cliente.getNome();
				}
			}
		}
		return nome;
	}

	/**
	 * Método externo para recuperar nome do vendedor
	 * 
	 * @param venda
	 * @return
	 */
	public String vendedor(Venda venda) {
		String nome = "";
		if (Util.validar(venda.getVendedor())) {
			nome = venda.getVendedor().getNome(); 
		}
		return nome;
	}

	/**
	 * Método externo para recuperar nome da loja
	 * 
	 * @param venda
	 * @return
	 */
	public String loja(Venda venda) {
		String nome = "";
		if (Util.validar(venda.getVendedor())) {
			if (Util.validar(venda.getVendedor().getLoja())) {
				nome = venda.getVendedor().getLoja().getNome(); 
			}
		}
		return nome;
	}
	
	/**
	 *	Método interno para buscar itens da venda
	 * 
	 * @param venda
	 * @return
	 */
	private List<ItemVenda> buscarItens(Venda venda) {
		List<ItemVenda> itens = null;
		if (Util.validar(venda)) {
			if (Util.validar(venda.getItens())) {
				itens = venda.getItens();
			} else {
				itens = itemVendaService.buscarItens(venda);
			}
		}
		return itens;
	}
	
	/**
	 * Método interno para recuperar pagamento
	 * 
	 * @param venda
	 * @return
	 */
	private Pagamento buscarPagamento(Venda venda) {
		Pagamento pagamento = null;
		if (Util.validar(venda.getPagamento())) {
			pagamento = venda.getPagamento();
		} else {
			pagamento = pagamentoService.pesquisarPorVenda(venda);
		}
		return pagamento;
	}
	
	/**
	 * Método externo para buscar o total pago
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal calcularTotalPago(List<Venda> vendas) {
		double total = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				Pagamento pagamento = buscarPagamento(venda);
				if (Util.validar(pagamento)) {
					if (Util.validar(pagamento.getTotalApagar())) {
						total = total + pagamento.getTotalApagar().doubleValue();
					}
				}
			}
		}
		return BigDecimal.valueOf(total);
	}
	
	/**
	 * Método externo para buscar o total de outros
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal calcularTotalEmOutros(List<Venda> vendas) {
		double total = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				Pagamento pagamento = buscarPagamento(venda);
				if (Util.validar(pagamento)) {
					if (Util.validar(pagamento.getOutros())) {
						total = total + pagamento.getOutros().doubleValue();
					}
				}
			}
		}
		return BigDecimal.valueOf(total);
	}
	
	/**
	 * Método externo para buscar o total em cheque
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal calcularTotalEmCheque(List<Venda> vendas) {
		double total = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				Pagamento pagamento = buscarPagamento(venda);
				if (Util.validar(pagamento)) {
					if (Util.validar(pagamento.getCheque())) {
						total = total + pagamento.getCheque().doubleValue();
					}
				}
			}
		}
		return BigDecimal.valueOf(total);
	}
	
	/**
	 * Método externo para buscar o total de desconto
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal calcularTotalDeDesconto(List<Venda> vendas) {
		double total = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				Pagamento pagamento = buscarPagamento(venda);
				if (Util.validar(pagamento)) {
					if (Util.validar(pagamento.getValorDesconto())) {
						total = total + pagamento.getValorDesconto().doubleValue();
					}
				}
			}
		}
		return BigDecimal.valueOf(total);
	}

	/**
	 * Método externo para buscar o total pago em dinheiro
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal calcularTotalEmDinheiro(List<Venda> vendas) {
		double total = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				Pagamento pagamento = buscarPagamento(venda);
				if (Util.validar(pagamento)) {
					if (Util.validar(pagamento.getDinheiro())) {
						if (Util.validar(pagamento.getTroco())) {
							double troco = pagamento.getTroco().doubleValue();
							double dinheiro = pagamento.getDinheiro().doubleValue();
							double diferenca = (dinheiro - troco); 
							total = total + diferenca;
						} else {
							total = total + pagamento.getDinheiro().doubleValue();
						}
					}
				}
			}
		}
		return BigDecimal.valueOf(total);
	}
	
	/**
	 * Método externo para buscar o total pago em cartão
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal[] calcularTotalEmCartao(List<Venda> vendas) {
		BigDecimal[] emCartao = { BigDecimal.ZERO, BigDecimal.ZERO };
		double totalDebito = 0.0d;
		double totalCredito = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				Pagamento pagamento = buscarPagamento(venda);
				if (Util.validar(pagamento)) {
					if (Util.validar(pagamento.getCartao())) {
						if (Util.validar(pagamento.getTipoPagamentoCartao())) {
							if (pagamento.getTipoPagamentoCartao().equals(TipoPagamentoCartao.DEBITO)) {
								totalDebito = totalDebito + pagamento.getCartao().doubleValue();
							}
							if (pagamento.getTipoPagamentoCartao().equals(TipoPagamentoCartao.CREDITO)) {
								totalCredito = totalCredito + pagamento.getCartao().doubleValue();
							}
						}
					}
				}
			}
		}
		emCartao[TIPO_ZERO_PAGAMENTO_DEBITO] = BigDecimal.valueOf(totalDebito);
		emCartao[TIPO_UM_PAGAMENTO_CREDITO] = BigDecimal.valueOf(totalCredito);
		return emCartao;
	}
	
	/**
	 * Método externo para buscar o total
	 * 
	 * @param vendas
	 * @return
	 */
	public BigDecimal calcularTotalVendas(List<Venda> vendas) {
		double total = 0.0d;
		if (Util.validar(vendas)) {
			for (Venda venda : vendas) {
				if (Util.validar(venda.getTotal())) {
					total = total + venda.getTotal().doubleValue();
				}
			}
		}
		return BigDecimal.valueOf(total);
	}
	
	/**
	 * Método externo para listar as vendas do dia do vendedor
	 * 
	 * @param vendedor
	 * @return
	 */
	public List<Venda> listarVendasDoDiaPorVendedor(Vendedor vendedor) {
		return vendaService.listarVendasPorVendedor(vendedor, new Date());
	}

	/**
	 * Método externo para listar as vendas do dia da loja
	 * 
	 * @param loja
	 * @return
	 */
	public List<Venda> listarVendasDoDiaPorLoja(Loja loja) {
		return vendaService.listarVendasPorLoja(loja, new Date());
	}
	
	/**
	 * Método para recuperar lista de vendas do dia por vendedor informado
	 *
	 * @param vendedor
	 * @return
	 */
	public List<VendaDiariaVO> listarVendasDoDia(List<Venda> vendas) {
		List<VendaDiariaVO> lista = null;
		if (Util.validar(vendas)) {
			lista = new ArrayList<>();
			for (Venda venda : vendas) {
				VendaDiariaVO vo = new VendaDiariaVO();
				if (Util.validar(venda.getCodigo())) {
					vo.setVenda(venda.getCodigo());
				}
				if (Util.validar(venda.getData())) {
					vo.setData(venda.getData());
				}
				if ((Util.validar(venda.getVendedor())) && (Util.validar(venda.getVendedor().getNome()))) {
					String vendedor = String.valueOf(venda.getVendedor().getCodigo()) + " - " + venda.getVendedor().getNome();
					vo.setVendedor(vendedor);
				}
				List<ItemVenda> itens = buscarItens(venda);
				if (Util.validar(itens)) {
					double total = 0;
					int soma = 0;
					for (ItemVenda itemVenda : itens) {
						if (Util.validar(itemVenda.getQuantidade())) {
							soma = soma + itemVenda.getQuantidade().intValue();
						}
						if (Util.validar(itemVenda.getSubTotal())) {
							total = total + itemVenda.getSubTotal().doubleValue();
						}
					}
					vo.setItens(Integer.valueOf(soma));
					vo.setTotal(BigDecimal.valueOf(total));
					lista.add(vo);
				}
			}
		}
		return lista;
	}

	/**
	 * Método externo para recuperar vendedor e loja
	 * 
	 * @param codigo
	 * @return
	 */
	public Vendedor buscarVendedorELoja(Long codigo) {
		Vendedor vendedor = null;
		if (Util.validar(codigo)) {
			Vendedor vendedorDB = vendedorService.pesquisarPorCodigo(codigo);
			if (Util.validar(vendedorDB)) {
				if (Util.validar(vendedorDB.getLoja())) {
					if (Util.validar(vendedorDB.getLoja().getNome())) {
						vendedor = vendedorDB;
					}
				}
			}
		}
		return vendedor;
	}

	/**
	 * Método externo para recuperar loja
	 * 
	 * @param codigo
	 * @return
	 */
	public Loja buscarLoja(Long codigo) {
		Loja loja = null;
		if (Util.validar(codigo)) {
			Loja lojaDB = lojaService.pesquisarPorCodigo(codigo);
			if (Util.validar(lojaDB)) {
				loja = lojaDB;
			}
		}
		return loja;
	}
	
}
