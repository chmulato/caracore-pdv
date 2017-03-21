package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.StatusVenda;
import br.com.caracore.pdv.repository.VendaRepository;
import br.com.caracore.pdv.repository.filter.VendaFilter;
import br.com.caracore.pdv.repository.filter.VendedorFilter;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.ProdutoNaoCadastradoException;
import br.com.caracore.pdv.util.Util;

@Service
public class VendaService {

	final private boolean PESQUISA_DEFAULT = true;
	
	final private int QUANTIDADE_UNITARIA = 1;

	final private double ZERO = 0.0d;
	
	final private double PORCENTAGEM = 100.0d;

	final private Date DATA_DE_HOJE = new Date();

	final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private PagamentoService pagamentoService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private OperadorService operadorService;

	@Autowired
	private LojaService lojaService;

	@Autowired
	private VendedorService vendedorService;

	@Autowired
	private ItemVendaService itemVendaService;

	/**
	 * Método para buscar vendas por parâmetros
	 * 
	 * @param codigo
	 * @param dataInicial
	 * @param dataFinal
	 * @param nomeVendedor
	 * @return
	 */
	public List<Venda> pesquisar(VendaFilter filtro) {
		List<Venda> lista = null;
		boolean pesquisaDefault = PESQUISA_DEFAULT;
		String nomeVendedor = null;
		String nomeLoja = null;
		Date dataInicial = null;
		Date dataFinal = null;
		if (filtro != null) {
			if (Util.validar(filtro.getVendedor())) {
				nomeVendedor = filtro.getVendedor();
				pesquisaDefault = false;
			}
			if (Util.validar(filtro.getLoja())) {
				nomeLoja = filtro.getLoja();
				pesquisaDefault = false;
			}
			if (Util.validar(filtro.getDataInicial())) {
				String data = filtro.getDataInicial();
				dataInicial = Util.dataHoraInicial(Util.formataData(data));
			}
			if (Util.validar(filtro.getDataFinal())) {
				String data = filtro.getDataFinal();
				dataFinal = Util.dataHoraFinal(Util.formataData(data));
			}
		}
		if (pesquisaDefault) {
			dataInicial = Util.dataHoraInicialDoMes(new Date());
			dataFinal = Util.dataHoraFinalDoMes(new Date());
			lista = vendaRepository.findByDataBetween(dataInicial, dataFinal);
		} else {
			Vendedor vendedor = vendedorService.pesquisar(nomeVendedor);
			Loja loja = lojaService.pesquisarPorNome(nomeLoja);
			lista = vendaRepository.findByDataBetweenAndVendedorAndLoja(dataInicial, dataFinal, vendedor, loja);
		}
		return lista;
	}

	/**
	 * Método interno para buscar o pagamento
	 * 
	 * @param venda
	 * @return
	 */
	private Venda buscarPagamento(Venda venda) {
		if (Util.validar(venda)) {
			Pagamento pagamento = pagamentoService.pesquisarPorVenda(venda);
			if (Util.validar(pagamento)) {
				venda.setPagamento(pagamento);
			}
		}
		return venda;
	}
	
	/**
	 * Método externo para pesquisar lista de vendedores
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Vendedor> pesquisar(VendedorFilter filtro) {
		return vendedorService.pesquisar(filtro);
	}

	/**
	 * Método externo para selecionar vendedor 
	 * 
	 * @param codigo
	 * @return
	 */
	public Vendedor selecionarPorId(Long codigo) {
		Vendedor vendedor = null;
		vendedor = vendedorService.pesquisarPorCodigo(codigo);
		return vendedor;
	}
	
	/**
	 * Método externo para recuperar ultimo item da cesta
	 * 
	 * @param venda
	 * @return
	 */
	public ItemVenda recuperarUltimoItemVendaDaCesta(Venda venda) {
		ItemVenda ultimoItem = null;
		if (Util.validar(venda) && Util.validar(venda.getCodigo())) {
			List <ItemVenda> itens = itemVendaService.buscarItens(venda);
			if (Util.validar(itens)) {
				long codigo = 0;
				long ultimoCodigo = 0;
				for (ItemVenda item : itens) {
					if (Util.validar(item.getCodigo())) {
						ultimoCodigo = item.getCodigo().longValue();
						if (ultimoCodigo > codigo) {
							ultimoItem = item;
							codigo = ultimoCodigo;
						}
					}
				}
			}
		}
		return ultimoItem;
	}
	
	
	/**
	 * Formatar Data
	 * 
	 * @param data
	 * @return
	 */
	public String formatarData(Date data) {
		String strData = "";
		if (Util.validar(data)) {
			strData = DATE_FORMAT.format(data);
		} else {
			strData = DATE_FORMAT.format(DATA_DE_HOJE.getTime());
		}
		return strData;
	}
	
	/**
	 * Metodo para recuperar venda em aberto informando o vendedor
	 * 
	 * @param vendedor
	 * @return
	 */
	public Venda recuperarVendaEmAberto(Vendedor vendedor) {
		Venda result = new Venda();
		result.setVendedor(vendedor);
		List<Venda> lista = vendaRepository.findByVendedorAndDataAndStatus(vendedor, DATA_DE_HOJE, StatusVenda.EM_ABERTO);
		if (Util.validar(lista)) {
			if (lista.size() == QUANTIDADE_UNITARIA) {
				for (Venda venda : lista) {
					if (!Util.validar(venda.getItens())) {
						List<ItemVenda> itens = itemVendaService.buscarItens(venda);
						if (Util.validar(itens)) {
							venda.setItens(itens);
						}
					}
					venda = totalizar(venda);
					result = venda;
				}
			}
		}
		return result;
	}
	
	/**
	 * Método para recuperar o vendedor do operador, 
	 * se não encontrar recupera o vendedor padrão da loja
	 * 
	 * @param operador
	 * @return
	 */
	public Vendedor buscarVendedor(Operador operador) {
		Vendedor vendedor = null;
		if (Util.validar(operador)) {
			vendedor = vendedorService.buscarPorOperador(operador);
			if (!Util.validar(vendedor)) {
				if (Util.validar(operador.getLoja())) {
					Loja loja = operador.getLoja();
					vendedor = vendedorService.buscarDefault(loja);
				}
			}
		}
		return vendedor;
	}
	
	/**
	 * Método interno para recuperar lista de vendedores da loja
	 * 
	 * @param loja
	 * @return
	 */
	private List<Vendedor> listarVendedoresPorLoja(Loja loja) {
		return lojaService.listarVendedores(loja);
	}

	/**
	 * Método externo para recuperar operador logado
	 * 
	 * @param login
	 * @return
	 */
	public Operador recuperarOperador(String login) {
		Operador operador = null;
		if (Util.validar(login)) {
			operador = operadorService.pesquisarPorNome(login); 
		}
		return operador;
	}
	
	/**
	 *	Método para validar se existe um venda com itens de compra
	 * 
	 * @param venda
	 * @return
	 */
	public boolean validarVendaEmAndamento(Venda venda) {
		boolean validar = false;
		if (Util.validar(venda)) {
			if (Util.validar(venda.getCodigo())) {
				if (Util.validar(venda.getItens())) {
					validar = true;
				}
			}
		}
		return validar;
	}
	/**
	 * Método para atualizar o desconto total da compra
	 * 
	 * @param pagamento
	 */
	public void salvarVendaPaga(Pagamento pagamento) {
		if (Util.validar(pagamento)) {
			if (Util.validar(pagamento.getTotalApagar()) && Util.validar(pagamento.getDesconto())) {
				Long codigo = pagamento.getVenda().getCodigo();
				BigDecimal valorPago = pagamento.getTotalApagar();
				BigDecimal desconto = pagamento.getDesconto();
				if ((desconto.doubleValue() < ZERO) || (desconto.doubleValue() > PORCENTAGEM)) {
					throw new DescontoInvalidoException("Desconto inválido!");
				}
				Venda venda = vendaRepository.findOne(codigo);
				if (Util.validar(venda)) {
					venda.setDescontoTotal(desconto);
					venda.setTotal(valorPago);
					venda.setStatus(StatusVenda.FINALIZADO);
					vendaRepository.save(venda);
				}
			}
		}
	}
	
	/**
	 * Método para recuperar lista de vendas por vendedor
	 * 
	 * @param vendedor
	 * @param dataInicial
	 * @return
	 */
	public List<Venda> listarVendasPorVendedor(Vendedor vendedor, Date data) {
		Date dataInicial = Util.dataHoraInicial(data);
		Date dataFinal = Util.dataHoraFinal(data);
		return vendaRepository.findByDataBetweenAndVendedorAndStatus(dataInicial, dataFinal, vendedor, StatusVenda.FINALIZADO);
	}

	/**
	 * Método para recuperar lista de vendas por loja
	 * 
	 * @param loja
	 * @param dataInicial
	 * @return
	 */
	public List<Venda> listarVendasPorLoja(Loja loja, Date data) {
		Date dataInicial = Util.dataHoraInicial(data);
		Date dataFinal = Util.dataHoraFinal(data);
		return vendaRepository.findByDataBetweenAndLojaAndStatus(dataInicial, dataFinal, loja, StatusVenda.FINALIZADO);
	}

	/**
	 * Método para atualizar o desconto total da compra
	 * 
	 * @param codigo
	 * @param desconto
	 */
	public void salvarVendedorEDescontoTotal(Long codigo, Vendedor vendedor, BigDecimal desconto) {
		if ((Util.validar(codigo)) && (Util.validar(desconto))) {
			if ((desconto.doubleValue() < ZERO) || (desconto.doubleValue() > PORCENTAGEM)) {
				throw new DescontoInvalidoException("Desconto inválido!");
			}
			Venda venda = vendaRepository.findOne(codigo);
			vendedor = vendedorService.pesquisarPorCodigo(vendedor.getCodigo());
			if (Util.validar(venda)) {
				venda.setDescontoTotal(desconto);
				venda.setVendedor(vendedor);
				vendaRepository.save(venda);
			}
		}
	}
	
	/**
	 * Método externo para salvar vendas. Salva a data atualizada de hoje 
	 * 
	 * @param venda
	 * @return
	 */
	public Venda salvar(Venda venda) {
		setarDataHoje(venda);
		venda = vendaRepository.save(venda);
		return venda;
	}

	/**
	 * Método interno para setar a data de hoje
	 * 
	 * @param venda
	 */
	private void setarDataHoje(Venda venda) {
		if (Util.validar(venda)) {
			venda.setData(DATA_DE_HOJE);
		}
	}

	/**
	 * Método externo para excluir uma venda
	 * 
	 * @param codigo
	 */
	public void excluir(Long codigo) {
		vendaRepository.delete(codigo);
	}

	/**
	 * Método externo para pesquisar uma venda
	 * 
	 * @param codigo
	 * @return
	 */
	public Venda pesquisarPorId(Long codigo) {
		Venda venda = vendaRepository.findOne(codigo);
		return buscarPagamento(venda);
	}
	
	/**
	 * Método para recuperar lista de vendedores da loja informando o operador da loja.
	 * No caso de nenhum operador for encontrado recupera vendedor gerente
	 * 
	 * @param operador
	 * @return
	 */
	public List<Vendedor> listarVendedoresPorOperador(Operador operador) {
		List<Vendedor> lista = null;
		if (Util.validar(operador) && Util.validar(operador.getLoja())) {
			Loja loja = operador.getLoja();
			Vendedor vendedor = vendedorService.buscarDefault(loja);
			if (Util.validar(vendedor)) {
				lista = listarVendedoresPorLoja(loja);
			}
		}
		return lista;
	}

	/**
	 * Método Interno para calcular o subTotal do item
	 * 
	 * @param item
	 * @return
	 */
	private BigDecimal subTotal(ItemVenda item) {
		double subTotal = 0;
		if (Util.validar(item)) {
			if (Util.validar(item.getPrecoUnitario()) 
					&& (Util.validar(item.getDesconto())) 
					&& (Util.validar(item.getQuantidade()))) {
				double precoTotal = item.getPrecoUnitario().doubleValue();
				double desconto = item.getDesconto().doubleValue();
				long intQuantidade = item.getQuantidade().longValue();
				if (desconto >= ZERO && desconto <= PORCENTAGEM) {
					precoTotal = precoTotal * intQuantidade;
					subTotal = precoTotal - (precoTotal * (desconto/PORCENTAGEM));
				}
			}
		}
		return BigDecimal.valueOf(subTotal);
	}
	
	/**
	 * Metodo interno para adicionar item de produto na cesta de compras
	 * 
	 * @param codigoProduto
	 * @param codigoBarra
	 * @return
	 */
	private ItemVenda carregarItem(Long codigoProduto, Integer quantidade, String codigoBarra) {
		Produto produto = null;
		ItemVenda item = new ItemVenda();
		if (Util.validar(codigoProduto)) {
			produto = produtoService.pesquisarPorCodigo(codigoProduto);
		} else if (Util.validar(codigoBarra)) {
			produto = produtoService.pesquisarPorCodigoBarra(codigoBarra);
		}
		if (Util.validar(produto)) {
			item.setDesconto(BigDecimal.ZERO);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getValor());
			if (Util.validar(quantidade)) {
				item.setQuantidade(quantidade);
			} else {
				item.setQuantidade(Integer.valueOf(QUANTIDADE_UNITARIA));
			}
			item.setSubTotal(subTotal(item));
		} else {
			throw new ProdutoNaoCadastradoException("Produto não cadastrado!");
		}
		return item;
	}
	
	/**
	 * Metodo externo para carregar os itens de venda na cesta
	 * 
	 * @param codigoProduto
	 * @param operador
	 * @return
	 */
	public Venda comprar(Long codigoProduto, Integer quantidade, String codigoBarra, Operador operador) {
		Venda result = null;
		ItemVenda novoItem = carregarItem(codigoProduto, quantidade, codigoBarra);
		if (Util.validar(operador)) {
			Vendedor vendedor = buscarVendedor(operador);
			if (Util.validar(vendedor)) {
				Venda venda = recuperarVendaEmAberto(vendedor);
				if (Util.validar(venda)) {

					List<ItemVenda> itens = new ArrayList<>();
					
					// setar valores defaults
					venda.setVendedor(vendedor);
					venda.setDescontoTotal(BigDecimal.ZERO);
					venda.setStatus(StatusVenda.EM_ABERTO);
					
					if (Util.validar(venda.getItens())) {
						itens = venda.getItens();
						itens.add(novoItem);
					} else {
						itens.add(novoItem);
					}
					
					venda.setItens(itens);
					venda = salvar(venda);
					
					if (Util.validar(venda.getCodigo())) {
						itens = itemVendaService.buscarItens(venda);
						itens.add(novoItem);
						itens = itemVendaService.salvarItens(itens, venda);
						if (Util.validar(itens)) {
							venda.setItens(itens);
						}
					}
				}
				venda = totalizar(venda);
				result = venda;
			}
		}
		return result;
	}

	
	/**
	 * Método interno para totalizar valores da compra
	 * 
	 * @param venda
	 * @return
	 */
	private Venda totalizar(Venda venda) {
		if (Util.validar(venda)) {
			double dblTotal = 0;
			if (Util.validar(venda.getItens())) {
				double total = 0L;
				for (ItemVenda item : venda.getItens()) {
					BigDecimal subTotal = subTotal(item);
					item.setSubTotal(subTotal);
					if (Util.validar(item.getSubTotal())) {
						total = total + item.getSubTotal().doubleValue();
					}
				}
				venda.setSubTotal(BigDecimal.valueOf(total));
			}
			if (Util.validar(venda.getSubTotal()) && Util.validar(venda.getDescontoTotal())) {
				double subTotal = venda.getSubTotal().doubleValue();
				double desconto = venda.getDescontoTotal().doubleValue();
				if (desconto >= ZERO && desconto <= PORCENTAGEM) {
					dblTotal = subTotal - (subTotal * (desconto/PORCENTAGEM));
				}
			}
			venda.setTotal(BigDecimal.valueOf(dblTotal));
		}
		return venda;
	}
}
