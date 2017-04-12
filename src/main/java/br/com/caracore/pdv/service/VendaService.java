package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.StatusVenda;
import br.com.caracore.pdv.model.types.TipoVendedor;
import br.com.caracore.pdv.repository.EstoqueRepository;
import br.com.caracore.pdv.repository.ItemVendaRepository;
import br.com.caracore.pdv.repository.LojaRepository;
import br.com.caracore.pdv.repository.OperadorRepository;
import br.com.caracore.pdv.repository.PagamentoRepository;
import br.com.caracore.pdv.repository.ProdutoRepository;
import br.com.caracore.pdv.repository.VendaRepository;
import br.com.caracore.pdv.repository.VendedorRepository;
import br.com.caracore.pdv.repository.filter.VendaFilter;
import br.com.caracore.pdv.repository.filter.VendedorFilter;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.LojaNaoEncontradaException;
import br.com.caracore.pdv.service.exception.ProdutoNaoCadastradoException;
import br.com.caracore.pdv.service.exception.QuantidadeInvalidaException;
import br.com.caracore.pdv.service.exception.QuantidadeNaoExistenteEmEstoqueException;
import br.com.caracore.pdv.service.exception.VendedorNaoEncontradoException;
import br.com.caracore.pdv.util.Util;
import br.com.caracore.pdv.vo.CompraVO;

@Service
public class VendaService {

	final public int QUANTIDADE_UNITARIA = 1;

	final private boolean PESQUISA_DEFAULT = true;

	final private double ZERO = 0.0d;

	final private double PORCENTAGEM = 100.0d;

	final private Date DATA_DE_HOJE = new Date();

	final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	@Autowired
	private EstoqueRepository estoqueRepository;

	@Autowired
	private ItemVendaRepository itemVendaRepository;

	@Autowired
	private ItemVendaService itemVendaService;

	@Autowired
	private LojaRepository lojaRepository;

	@Autowired
	private OperadorRepository operadorRepository;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	/**
	 * Método externo para apagar dados da sessão
	 * 
	 */
	public void apagarVendaDaSessão() {
		if (Util.validar(sessionService.getSessionVO())) {
			CompraVO sessao = sessionService.getSessionVO();
			if (Util.validar(sessao.getOperador())) {
				Operador operador = sessao.getOperador();
				sessionService.setSession(operador, null, null);
			}
		}
	}

	/**
	 * Método externo para recuperar dados da sessão
	 * 
	 * @return
	 */
	public CompraVO recuperarSessao() {
		CompraVO compraVO = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getName() != null) {
			String login = auth.getName();
			Operador operador = recuperarOperador(login);
			if (Util.validar(operador)) {
				sessionService.setSession(operador);
				compraVO = sessionService.getSessionVO();
			}
		}
		return compraVO;
	}

	
	/**
	 * Método publico para recuperar produto por código
	 * 
	 * @param codigo
	 * @return
	 */
	public Produto recuperarProdutoPorCodigo(Long codigo) {
		return produtoRepository.findOne(codigo);
	}
	
	
	/**
	 * Método publico para recuperar produto por código de barra
	 * 
	 * @param codigo
	 * @return
	 */
	public Produto recuperarProdutoPorCodigoBarra(String codigoBarra) {
		return produtoRepository.findByCodigoBarra(codigoBarra);
	}
	
	/**
	 * Método externo para salvar dados na sessão
	 * 
	 * @param venda
	 */
	public void salvarVendaNaSessao(Venda venda) {
		if ((Util.validar(venda)) && (Util.validar(venda.getVendedor()))) {
			Vendedor vendedor = venda.getVendedor();
			CompraVO vo = recuperarSessao();
			if ((Util.validar(vo)) && (Util.validar(vo.getOperador()))) {
				sessionService.setSession(vo.getOperador(), venda, vendedor);
			}
		}
	}

	/**
	 * Método externo para salvar dados na sessão
	 * 
	 * @param codigoVendedor
	 */
	public void salvarVendedorNaSessao(Vendedor vendedor) {
		if (Util.validar(vendedor)) {
			CompraVO vo = recuperarSessao();
			if ((Util.validar(vo)) && (Util.validar(vo.getOperador()))) {
				sessionService.setSession(vo.getOperador(), vendedor);
			}
		}
	}
	
	/**
	 * Método para cancelar venda
	 * 
	 * @param codigo
	 */
	public void cancelar(Long codigo) {
		if (Util.validar(codigo)) {
			Venda venda = vendaRepository.findOne(codigo);
			if (Util.validar(venda)) {
				venda.setStatus(StatusVenda.CANCELADA);
				venda = vendaRepository.save(venda);
			}
		}
	}
	
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
			if ((!Util.validar(dataInicial)) || (!Util.validar(dataFinal))) {
				dataInicial = Util.dataHoraInicialDoMes(new Date());
				dataFinal = Util.dataHoraFinalDoMes(new Date());
			}
			lista = vendaRepository.findByDataBetweenByOrderByDataDesc(dataInicial, dataFinal);
		} else {
			if ((!Util.validar(dataInicial) && (!Util.validar(dataFinal)))) {
				dataInicial = Util.dataHoraInicialDoMes(new Date());
				dataFinal = Util.dataHoraFinalDoMes(new Date());
			}
			Vendedor vendedor = this.pesquisar(nomeVendedor);
			Loja loja = this.pesquisarLojaPorNome(nomeLoja);
			if ((Util.validar(vendedor)) && (Util.validar(loja))) {
				lista = vendaRepository.findByDataBetweenAndVendedorAndLojaByOrderByDataDesc(dataInicial, dataFinal, vendedor, loja);
			}
			if ((Util.validar(vendedor)) && (!Util.validar(loja))) {
				lista = vendaRepository.findByDataBetweenAndVendedorByOrderByDataDesc(dataInicial, dataFinal, vendedor);
			}
			if ((!Util.validar(vendedor)) && (Util.validar(loja))) {
				lista = vendaRepository.findByDataBetweenAndLojaByOrderByDataDesc(dataInicial, dataFinal, loja);
			}
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
			Pagamento pagamento = this.pesquisarPagamentoPorVenda(venda);
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
		return this.pesquisarVendedor(filtro);
	}

	/**
	 * Método externo para recuperar vendedor
	 * 
	 * @param codigo
	 * @return
	 */
	public Vendedor recuperarVendedorPorId(Long codigo) {
		Vendedor vendedor = null;
		if (Util.validar(codigo)) {
			vendedor = this.pesquisarPorCodigo(codigo);
		}
		return vendedor;
	}

	/**
	 * Método externo para recuperar operador
	 * 
	 * @param codigo
	 * @return
	 */
	public Operador recuperarOperadorPorId(Long codigo) {
		Operador operador = null;
		if (Util.validar(codigo)) {
			operador = this.pesquisarOperadorPorId(codigo);
		}
		return operador;
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
			List<ItemVenda> itens = this.buscarItens(venda);
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
	 * Metodo para recuperar venda em aberto informando codigo da venda
	 * 
	 * @param vendedor
	 * @return
	 */
	public Venda recuperarVendaEmAberto(Vendedor vendedor) {
		Venda result = new Venda();
		List<Venda> lista = vendaRepository.findByVendedorAndDataAndStatus(vendedor, DATA_DE_HOJE, StatusVenda.EM_ABERTO);
		if (Util.validar(lista)) {
			if (lista.size() == QUANTIDADE_UNITARIA) {
				for (Venda venda : lista) {
					if (!Util.validar(venda.getItens())) {
						List<ItemVenda> itens = this.buscarItens(venda);
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
	 * Metodo para recuperar venda em aberto informando codigo da venda
	 * 
	 * @param codigoVenda
	 * @return
	 */
	public Venda recuperarVendaEmAberto(Long codigoVenda) {
		Venda result = new Venda();
		List<Venda> lista = vendaRepository.findByCodigoAndDataAndStatus(codigoVenda, DATA_DE_HOJE, StatusVenda.EM_ABERTO);
		if (Util.validar(lista)) {
			if (lista.size() == QUANTIDADE_UNITARIA) {
				for (Venda venda : lista) {
					if (!Util.validar(venda.getItens())) {
						List<ItemVenda> itens = this.buscarItens(venda);
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
	 * Método interno para recuperar o vendedor pro codigo do vendedor
	 * 
	 * @param codigoVendedor
	 * @return
	 */
	public Vendedor buscarVendedorPorCodigo(Long codigoVendedor) {
		Vendedor vendedor = null;
		if (Util.validar(codigoVendedor)) {
			vendedor = this.pesquisarPorCodigo(codigoVendedor);
		}
		return vendedor;
	}
	
	/**
	 * Método interno para buscar vendedor por loja
	 * 
	 * @param loja
	 * @return
	 */
	private Vendedor buscarDefault(Loja loja) {
		Vendedor vendedor = null;
		TipoVendedor tipo = TipoVendedor.DEFAULT;
		vendedor = vendedorRepository.findByTipoAndLoja(tipo, loja);
		return vendedor;
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
			operador = this.pesquisarOperadorPorNome(login);
		}
		return operador;
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
			vendedor = this.pesquisarPorCodigo(vendedor.getCodigo());
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
	 * Método para recuperar lista de vendedores da loja informando o operador
	 * da loja. No caso de nenhum operador for encontrado recupera vendedor
	 * gerente
	 * 
	 * @param operador
	 * @return
	 */
	public List<Vendedor> listarVendedoresPorOperador(Operador operador) {
		List<Vendedor> lista = null;
		if (Util.validar(operador) && Util.validar(operador.getLoja())) {
			Loja loja = operador.getLoja();
			Vendedor vendedor = this.buscarDefault(loja);
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
			if (Util.validar(item.getPrecoUnitario()) && (Util.validar(item.getDesconto()))
					&& (Util.validar(item.getQuantidade()))) {
				double precoTotal = item.getPrecoUnitario().doubleValue();
				double desconto = item.getDesconto().doubleValue();
				long intQuantidade = item.getQuantidade().longValue();
				if (desconto >= ZERO && desconto <= PORCENTAGEM) {
					precoTotal = precoTotal * intQuantidade;
					subTotal = precoTotal - (precoTotal * (desconto / PORCENTAGEM));
				}
			}
		}
		return BigDecimal.valueOf(subTotal);
	}

	/**
	 * Metodo interno para adicionar item de produto na cesta de compras
	 * 
	 * @param produto
	 * @param quantidade
	 * @return
	 */
	private ItemVenda carregarItem(Produto produto, Integer quantidade) {
		ItemVenda item = new ItemVenda();
		if (Util.validar(produto.getCodigo())) {
			Long codigoProduto = produto.getCodigo();
			produto = produtoRepository.findOne(codigoProduto);
		} else if (Util.validar(produto.getCodigoBarra())) {
			String codigoBarra = produto.getCodigoBarra();
			produto = produtoRepository.findByCodigoBarra(codigoBarra);
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
	 * Método interno para validar se existe o produto em estoque
	 * 
	 * @param produto
	 * @param loja
	 * @param quantidade
	 */
	private void validarProdutoEmEstoque(Produto produto, Loja loja, Integer quantidade) {
		long codigoProduto = 0;
		if ((Util.validar(produto)) && (Util.validar(produto.getCodigo()))) {
			codigoProduto = produto.getCodigo().longValue();
		}
		CompraVO session = sessionService.getSessionVO();
		if (Util.validar(session)) {
			if (Util.validar(session.getVenda())) {
				Venda venda = session.getVenda();
				List<ItemVenda> itens = this.buscarItens(venda);
				if (Util.validar(itens)) {
					int _quantidade = 0;
					for (ItemVenda item : itens) {
						if ((Util.validar(item)) && (Util.validar(item.getProduto()))) {
							long _codigoProduto = item.getProduto().getCodigo().longValue();
							if (codigoProduto == _codigoProduto) {
								_quantidade = item.getQuantidade().intValue() + _quantidade;
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
					throw new QuantidadeNaoExistenteEmEstoqueException("Quantidade de produto não existente em estoque!");
				}
			} else {
				throw new QuantidadeInvalidaException("Quantidade invalida!");
			}
		} else {
			throw new QuantidadeNaoExistenteEmEstoqueException("Quantidade de produto não existente em estoque!");
		}
	}
	
	/**
	 * Metodo externo para carregar os itens de venda na cesta
	 * 
	 * @param produto
	 * @param quantidade
	 * @param codigoVenda
	 * @param vendedor
	 * @return
	 */
	public Venda comprar(Produto produto, Integer quantidade, Long codigoVenda, Vendedor vendedor) {
		
		Loja loja = null;
		Venda result = null;
		
		if (!Util.validar(vendedor)) {
			throw new VendedorNaoEncontradoException("Vendedor não encontrado!");
		}

		if (!Util.validar(produto)) {
			throw new ProdutoNaoCadastradoException("Produto não cadastrado!");
		}
		
		if (!Util.validar(vendedor.getLoja())) {
			throw new LojaNaoEncontradaException("Loja não encontrada!");
		} else {
			loja = vendedor.getLoja();
		}
		
		if ((Util.validar(produto)) && (Util.validar(loja)) && (Util.validar(quantidade))) {
			validarProdutoEmEstoque(produto, loja, quantidade);
		}
		
		if (Util.validar(produto)) {
		
			ItemVenda novoItem = carregarItem(produto, quantidade);
			Venda venda = recuperarVendaEmAberto(codigoVenda);
			
			if (Util.validar(venda)) {
				List<ItemVenda> itens = new ArrayList<>();
				venda.setDescontoTotal(BigDecimal.ZERO);
				venda.setStatus(StatusVenda.EM_ABERTO);
				if (Util.validar(venda.getItens())) {
					itens = venda.getItens();
					itens.add(novoItem);
				} else {
					itens.add(novoItem);
				}
				if (Util.validar(vendedor)) {
					venda.setVendedor(vendedor);
				}
				venda.setItens(itens);
				venda = salvar(venda);
				if (Util.validar(venda.getCodigo())) {
					itens = this.buscarItens(venda);
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
					dblTotal = subTotal - (subTotal * (desconto / PORCENTAGEM));
				}
			}
			venda.setTotal(BigDecimal.valueOf(dblTotal));
		}
		return venda;
	}

	/**
	 * Pesquisar vendedor por nome 
	 * 
	 * @param nomeVendedor
	 * @return
	 */
	private Vendedor pesquisar(String nomeVendedor) {
		Vendedor result = null;
		if (Util.validar(nomeVendedor)) {
			List<Vendedor> lista = vendedorRepository.findByNomeContainingIgnoreCase(nomeVendedor);
			if (Util.validar(lista)) {
				for (Vendedor vendedor : lista) {
					result = vendedor;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Pesquisar vendedor por filtro 
	 * 
	 * @param filtro
	 * @return
	 */
	private List<Vendedor> pesquisarVendedor(VendedorFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return vendedorRepository.findByNomeContainingIgnoreCase(nome);
	}

	/**
	 * Pesquisar vendedor por código de vendedor
	 * 
	 * @param codigo
	 * @return
	 */
	private Vendedor pesquisarPorCodigo(Long codigo) {
		return vendedorRepository.findOne(codigo);
	}

	/**
	 * Recuperar lista de itens de venda por venda
	 * 
	 * @param venda
	 * @return
	 */
	private List<ItemVenda> buscarItens(Venda venda) {
		List<ItemVenda> itens = null;
		if (Util.validar(venda)) {
			itens = itemVendaRepository.findByVenda(venda);
		}
		return itens;
	}
	
	/**
	 * Método interno para recuperar lista de vendedores da loja
	 * 
	 * @param loja
	 * @return
	 */
	private List<Vendedor> listarVendedoresPorLoja(Loja loja) {
		return vendedorRepository.findByLoja(loja);
	}
	
	/**
	 * Método interno para recuperar loja por nome
	 * 
	 * @param nome
	 * @return
	 */
	private Loja pesquisarLojaPorNome(String nome) {
		return lojaRepository.findByNomeIgnoreCase(nome);
	}

	/**
	 * Método interno para recuperar operador por id
	 * 
	 * @param codigo
	 * @return
	 */
	private Operador pesquisarOperadorPorId(Long codigo) {
		return operadorRepository.findOne(codigo);
	}
	
	
	private Operador pesquisarOperadorPorNome(String login) {
		Operador operador = null;
		List<Operador> operadores = pesquisarPorLogin(login);
		if (Util.validar(operadores)) {
			for (Operador _operador : operadores) {
				operador = _operador;
				break;
			}
		}
		return operador;
	}
	
	/**
	 * Método interno para recuperar operador por login
	 * 
	 * @param login
	 * @return
	 */
	private List<Operador> pesquisarPorLogin(String login) {
		return operadorRepository.findByNomeContainingIgnoreCase(login);
	}

	/**
	 * Método interno para pesquisar pagamento por venda
	 * 
	 * @param venda
	 * @return
	 */
	private Pagamento pesquisarPagamentoPorVenda(Venda venda) {
		return pagamentoRepository.findByVenda(venda);
	}
}
