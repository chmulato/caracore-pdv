package br.com.caracore.pdv.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.repository.PagamentoRepository;
import br.com.caracore.pdv.service.exception.CpfExistenteException;
import br.com.caracore.pdv.service.exception.CpfInvalidoException;
import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.service.exception.PagamentoInvalidoException;
import br.com.caracore.pdv.service.exception.TipoPagamentoCartaoInvalidoException;
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
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private VendaService vendaService;

	/**
	 * Método interno para validar cpf e se o cpf informado corresponde ao
	 * cliente do pagamento.
	 * 
	 * @param pagamento
	 */
	private void validarCpfDeCliente(Pagamento pagamento) {
		if (Util.validar(pagamento)) {
			if ((Util.validar(pagamento.getCpf()) && (Util.validar(pagamento.getCliente())))) {
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
		double cartao = 0d;
		double cheque = 0d;
		double outros = 0d;
		validarPorcentagem(pagamento);
		if (Util.validar(pagamento)) {
			if (Util.validar(pagamento.getTotalApagar())) {
				totalApagar = pagamento.getTotalApagar().doubleValue();
			}
			if (Util.validar(pagamento.getDinheiro())) {
				dinheiro = pagamento.getDinheiro().doubleValue();
			}
			if (Util.validar(pagamento.getCartao())) {
				cartao = pagamento.getCartao().doubleValue();
				if (!Util.validar(pagamento.getTipoPagamentoCartao())) {
					throw new TipoPagamentoCartaoInvalidoException("Débito ou Crédito?");
				}
			}
			if (Util.validar(pagamento.getCheque())) {
				cheque = pagamento.getCheque().doubleValue();
			}
			if (Util.validar(pagamento.getOutros())) {
				outros = pagamento.getOutros().doubleValue();
			}
			valores = dinheiro + cartao + cheque + outros;
			troco = valores - totalApagar;
			if (troco < ZERO) {
				throw new ValorInvalidoException("Valores inválidos!");
			}
			troco = Util.round(troco, DUAS_CASAS_DECIMAIS);
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
				vendaService.salvarVendaPaga(pagamento);
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

}
