package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.StatusVenda;
import br.com.caracore.pdv.repository.EstoqueRepository;
import br.com.caracore.pdv.repository.ItemVendaRepository;
import br.com.caracore.pdv.repository.PagamentoRepository;
import br.com.caracore.pdv.repository.VendaRepository;
import br.com.caracore.pdv.repository.VendedorRepository;
import br.com.caracore.pdv.service.exception.CpfExistenteException;
import br.com.caracore.pdv.service.exception.CpfInvalidoException;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.LojaNaoEncontradaException;
import br.com.caracore.pdv.service.exception.PagamentoInvalidoException;
import br.com.caracore.pdv.service.exception.QuantidadeNaoExistenteEmEstoqueException;
import br.com.caracore.pdv.service.exception.TrocoInvalidoException;
import br.com.caracore.pdv.service.exception.ValorInvalidoException;
import br.com.caracore.pdv.util.Util;

@Service
public class PagamentoService {

	final int DUAS_CASAS_DECIMAIS = 2;
	
	final double ZERO = 0.0d;

	final double PORCENTAGEM = 100.0;

	final private String CLIENTE_NAO_INFORMADO = "NAO_INFORMADO";

	final private boolean CPF_JA_CADASTRADO = true;

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private EstoqueRepository estoqueRepository;
	
	@Autowired
	private ItemVendaRepository itemVendaRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	/**
	 * Método interno para validar cpf e se o cpf informado corresponde ao
	 * cliente do pagamento.
	 * 
	 * @param pagamento
	 */
	private void validarCpfDeCliente(Pagamento pagamento) {
		if (Util.validar(pagamento)) {
			if (Util.validar(pagamento.getCpf())) {
				String cpf = pagamento.getCpf();
				// cpf = Util.removerFormatoCpf(cpf);
				if (!Util.isCPF(cpf)) {
					throw new CpfInvalidoException("Cpf inválido!");
				}
				Cliente clienteDB = clienteService.pesquisarPorCpf(cpf);
				if (Util.validar(clienteDB)) {
					if (Util.validar(clienteDB.getCpf())) {
						String cpfDB = clienteDB.getCpf();
						if (!cpf.equals(cpfDB)) {
							throw new CpfInvalidoException("CPF diferente do cliente informado!");
						} else {
							pagamento.setCliente(clienteDB);
						}
					}
				}
			}
		} else {
			throw new PagamentoInvalidoException("Pagamento inválido!");
		}
	}

	/**
	 * Método interno para validar desconto
	 * 
	 * @param pagamento
	 */
	private void validarPorcentagem(Pagamento pagamento) {
		double subTotal = 0d;
		double desconto = 0d;
		double valorDesconto = 0d;
		double totalAPagar = 0d;
		if (Util.validar(pagamento)) {
			if (Util.validar(pagamento.getSubTotal())) {
				subTotal = pagamento.getSubTotal().doubleValue();
			} else {
				throw new ValorInvalidoException("Valor inválido!");
			}
			if (Util.validar(pagamento.getDesconto())) {
				desconto = pagamento.getDesconto().doubleValue();
				if ((desconto < ZERO) || (desconto > PORCENTAGEM)) {
					throw new DescontoInvalidoException("Desconto inválido!");
				}
			} else {
				throw new DescontoInvalidoException("Desconto inválido!");
			}
		} else {
			throw new PagamentoInvalidoException("Pagamento inválido!");
		}
		valorDesconto = ((subTotal * desconto) / PORCENTAGEM);
		totalAPagar = (subTotal - valorDesconto);
		pagamento.setValorDesconto(BigDecimal.valueOf(valorDesconto));
		pagamento.setTotalApagar(BigDecimal.valueOf(totalAPagar));
	}

	/**
	 * Método interno para validar o pagamento
	 * 
	 * @param pagamento
	 */
	private void validarPagamento(Pagamento pagamento) {
		double troco = 0d;
		double totalApagar = 0d;
		double valores = 0d;
		double dinheiro = 0d;
		double debito = 0d;
		double credito = 0d;
		double outros = 0d;
		validarPorcentagem(pagamento);
		if (Util.validar(pagamento)) {
			if (Util.validar(pagamento.getTotalApagar())) {
				totalApagar = pagamento.getTotalApagar().doubleValue();
			}
			if (Util.validar(pagamento.getDinheiro())) {
				dinheiro = pagamento.getDinheiro().doubleValue();
			}
			if (Util.validar(pagamento.getDebito())) {
				debito = pagamento.getDebito().doubleValue();
			}
			if (Util.validar(pagamento.getCredito())) {
				credito = pagamento.getCredito().doubleValue();
			}
			if (Util.validar(pagamento.getOutros())) {
				outros = pagamento.getOutros().doubleValue();
			}
			valores = dinheiro + debito + credito + outros;
			troco = valores - totalApagar;
			troco = Util.round(troco, DUAS_CASAS_DECIMAIS);
			if (troco < ZERO) {
				throw new ValorInvalidoException("Valores inválidos!");
			}
			if ((dinheiro == ZERO) && (troco > ZERO)) {
				throw new TrocoInvalidoException("Troco somente com pagamento em dinheiro!");
			}
			pagamento.setTroco(BigDecimal.valueOf(troco));
		}
	}

	/**
	 * Método externo para pesquisar pagamento por código Id
	 * 
	 * @param codigo
	 * @return
	 */
	public Pagamento pesquisarPorCodigo(Long codigo) {
		Pagamento pagamento = pagamentoRepository.findOne(codigo);
		if (Util.validar(pagamento)) {
			if (Util.validar(pagamento.getCpf())) {
				String cpf = pagamento.getCpf();
				Cliente cliente = clienteService.pesquisarPorCpf(cpf);
				if (Util.validar(cliente)) {
					pagamento.setCliente(cliente);
				}
			}
		}
		return pagamento;
	}

	/**
	 * Método externo para pesquisar pagamento por venda
	 * 
	 * @param venda
	 * @return
	 */
	public Pagamento pesquisarPorVenda(Venda venda) {
		return pagamentoRepository.findByVenda(venda);
	}

	/**
	 * Método externo para salvar pagamento
	 * 
	 * @param pagamento
	 * @return
	 */
	public Pagamento pagar(Pagamento pagamento) {
		validarCpfDeCliente(pagamento);
		validarPagamento(pagamento);
		pagamento = pagamentoRepository.save(pagamento);
		if ((Util.validar(pagamento)) && (Util.validar(pagamento.getVenda()))) {
			if (Util.validar(pagamento.getVenda().getCodigo())) {
				salvarVendaPaga(pagamento);
			}
		}
		return pagamento;
	}

	/**
	 * Método externo para salvar pagamento
	 * 
	 * @param pagamento
	 * @param cliente
	 * @return
	 */
	public Pagamento salvar(Pagamento pagamento, Cliente cliente) {
		if ((Util.validar(pagamento)) && (Util.validar(cliente))) {
			if (!Util.validar(cliente.getNome())) {
				cliente = clienteService.pesquisarClienteDefault(CLIENTE_NAO_INFORMADO);
				if (!Util.validar(cliente)) {
					cliente = new Cliente(CLIENTE_NAO_INFORMADO);
					clienteService.salvar(cliente);
				}
			}
		}
		pagamento.setCliente(cliente);
		return pagamentoRepository.save(pagamento);
	}

	/**
	 * Método externo para salvar cliente na tela de pagamento
	 * 
	 * @param cliente
	 * @return
	 */
	public Cliente salvarCliente(Cliente cliente) {
		try {
			cliente = clienteService.salvar(cliente);
		} catch (CpfExistenteException ex) {
			cliente = clienteService.pesquisarPorCpf(cliente.getCpf());
			cliente.setThereIs(CPF_JA_CADASTRADO);
		}
		return cliente;
	}

	/**
	 * Método externo para recuperar cliente por cpf
	 * 
	 * @param cpf
	 */
	public Cliente buscarCliente(String cpf) {
		cpf = Util.removerFormatoCpf(cpf);
		return clienteService.pesquisarPorCpf(cpf);
	}

	/**
	 * Método interno para recuperar loja
	 * 
	 * @param venda
	 * @return
	 */
	private Loja recuperarLoja(Venda venda) {
		Loja loja = null;
		if (Util.validar(venda)) {
			if ((Util.validar(venda.getVendedor())) && (Util.validar(venda.getVendedor().getCodigo()))) {
				if (Util.validar(venda.getVendedor().getLoja())) {
					loja = venda.getVendedor().getLoja();
				} else {
					Long codigo = venda.getVendedor().getCodigo();
					Vendedor vendedor = vendedorRepository.findOne(codigo);
					if (Util.validar(vendedor)) {
						loja = vendedor.getLoja();
					}
				}
			}
		}
		return loja;
	}
	
	/**
	 * 
	 * Método interno para atualizar estoque
	 * 
	 * @param venda
	 */
	private void atualizarEstoque(Venda venda) {
		Loja loja = recuperarLoja(venda);
		if (Util.validar(loja)) {
			if ((Util.validar(venda)) && (Util.validar(venda.getItens()))) {
				List<ItemVenda> itens = itemVendaRepository.findByVenda(venda);
				if (Util.validar(itens)) {
					for (ItemVenda item : itens) {
						if (Util.validar(item)) {
							if ((Util.validar(item.getProduto())) && (Util.validar(item.getQuantidade()))) {
								Produto produto = item.getProduto();
								int quantidadeComprada = item.getQuantidade().intValue();
								Estoque estoque = estoqueRepository.findByLojaAndProduto(loja, produto);
								if ((Util.validar(estoque)) && (Util.validar(estoque.getQuantidade()))) {
									int quantidadeEstoque = estoque.getQuantidade().intValue();
									quantidadeEstoque = quantidadeEstoque - quantidadeComprada;
									if (quantidadeEstoque >= 0) {
										estoque.setQuantidade(quantidadeEstoque);
										estoqueRepository.save(estoque);
									} else {
										throw new QuantidadeNaoExistenteEmEstoqueException("Quantidade de produto não existente em estoque!");
									}
								}
							}
						}
					}
				}
			}
		} else {
			throw new LojaNaoEncontradaException("Loja não encontrada!");
		}
	}
	
	/**
	 * Método interno para atualizar o desconto total da compra
	 * 
	 * @param pagamento
	 */
	private void salvarVendaPaga(Pagamento pagamento) {
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
					atualizarEstoque(venda);
					venda.setDescontoTotal(desconto);
					venda.setTotal(valorPago);
					venda.setStatus(StatusVenda.FINALIZADO);
					vendaRepository.save(venda);
				}
			}
		}
	}

}
