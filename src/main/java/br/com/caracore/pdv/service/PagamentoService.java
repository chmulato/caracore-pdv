package br.com.caracore.pdv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.repository.PagamentoRepository;
import br.com.caracore.pdv.util.Util;

@Service
public class PagamentoService {

	final private String CLIENTE_NAO_INFORMADO = "NAO_INFORMADO";
	
	@Autowired
	private ClienteService clienteService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	/**
	 * Método externo para pesquisar pagamento por código Id
	 * 
	 * @param codigo
	 * @return
	 */
	public Pagamento pesquisarPorCodigo(Long codigo) {
		return pagamentoRepository.findOne(codigo);
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
	 * Método  externo para salvar pagamento
	 * 
	 * @param pagamento
	 * @param cliente
	 */
	public void salvar(Pagamento pagamento, Cliente cliente) {
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
		pagamentoRepository.save(pagamento);
	}
	
	/**
	 * Método externo para salvar cliente na tela de pagamento
	 * 
	 * @param cliente
	 */
	public Cliente salvarCliente(Cliente cliente) {
		return clienteService.salvar(cliente);
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
