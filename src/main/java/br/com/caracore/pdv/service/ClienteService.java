package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.repository.ClienteRepository;
import br.com.caracore.pdv.repository.filter.ClienteFilter;
import br.com.caracore.pdv.service.exception.CpfExistenteException;
import br.com.caracore.pdv.service.exception.CpfInvalidoException;
import br.com.caracore.pdv.service.exception.NomeExistenteException;
import br.com.caracore.pdv.service.exception.ViolacaoIntegridadeException;
import br.com.caracore.pdv.util.Util;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	/**
	 * Método interno para pesquisar cliente por nome
	 * 
	 * @param nome
	 * @return
	 */
	private Cliente pesquisarPorNome(String nome) {
		return clienteRepository.findByNomeIgnoreCase(nome);
	}

	/**
	 * Método interno para validar cpf do cliente
	 * 
	 * @param cliente
	 */
	private void validarCpf(Cliente cliente) {
		if (Util.validar(cliente) && Util.validar(cliente.getCpf())) {
			String strCpf = String.valueOf(cliente.getCpf());
			if (!Util.isCPF(strCpf)) {
				throw new CpfInvalidoException("Cpf inválido!");
			}
		}
		if (Util.validar(cliente) && Util.validar(cliente.getCpf())) {
			Cliente clienteDB = pesquisarPorCpf(cliente.getCpf());
			if (Util.validar(clienteDB)) {
				if (Util.validar(cliente.getCodigo())) {
					long codigo = cliente.getCodigo().longValue();
					long codigoDB = clienteDB.getCodigo().longValue();
					if (codigo != codigoDB) {
						throw new CpfExistenteException("Cpf já existente!");
					}
				} else {
					throw new CpfExistenteException("Cpf já existente!");
				}
			}
		}
	}

	/**
	 * Método interno para validar nome do cliente
	 * 
	 * @param cliente
	 */
	private void validarNome(Cliente cliente) {
		if (Util.validar(cliente) && Util.validar(cliente.getNome())) {
			Cliente clienteDB = pesquisarPorNome(cliente.getNome());
			if (Util.validar(clienteDB)) {
				if (Util.validar(cliente.getCodigo())) {
					long codigo = cliente.getCodigo().longValue();
					long codigoDB = clienteDB.getCodigo().longValue();
					if (codigo != codigoDB) {
						throw new NomeExistenteException("Nome já existente!");
					}
				} else {
					throw new NomeExistenteException("Nome já existente!");
				}
			}
		}
	}

	/**
	 * Método externo para pesquisar o cliente DEFAULT
	 * ou seja, não informado 
	 * 
	 * @param strDefault
	 * @return
	 */
	public Cliente pesquisarClienteDefault(String strDefault) {
		return pesquisarPorNome(strDefault);
	}

	/**
	 * Método externo para pesquisar cliente por CPF
	 * 
	 * @param strCpf
	 * @return
	 */
	public Cliente pesquisarPorCpf(String cpf) {
		return clienteRepository.findByCpf(cpf);
	}
	
	/**
	 * Método externo para salvar cliente
	 * 
	 * @param cliente
	 * @return
	 */
	public Cliente salvar(Cliente cliente) {
		validarCpf(cliente);
		validarNome(cliente);
		return clienteRepository.save(cliente);
	}

	/**
	 * Método para excluir cliente pelo código Id
	 * 
	 * @param codigo
	 */
	public void excluir(Long codigo) {
		try {
			clienteRepository.delete(codigo);
		} catch (DataIntegrityViolationException ex) {
			throw new ViolacaoIntegridadeException("Relacionamento encontrado! Cliente não pode ser excluído.");
		}
	}

	/**
	 * Método externo para pesquisar cliente pelo código Id
	 * 
	 * @param codigo
	 * @return
	 */
	public Cliente pesquisarPorCodigo(Long codigo) {
		return clienteRepository.findOne(codigo);
	}

	public List<Cliente> pesquisar(ClienteFilter filtro) {
		List<Cliente> lista = null;
		String nome = "%";
		if (Util.validar(filtro) && Util.validar(filtro.getNome())) {
			nome = filtro.getNome();
			if (Util.validar(nome)) {
				nome = filtro.getNome();
			}
		}
		lista = clienteRepository.findByNomeContainingIgnoreCase(nome);
		return lista;
	}

}
