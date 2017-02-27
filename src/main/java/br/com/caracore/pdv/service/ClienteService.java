package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.repository.ClienteRepository;
import br.com.caracore.pdv.repository.filter.ClienteFilter;
import br.com.caracore.pdv.service.exception.NomeExistenteException;
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

	public void salvar(Cliente cliente) {
		validarNome(cliente);
		clienteRepository.save(cliente);
	}

	public void excluir(Long codigo) {
		clienteRepository.delete(codigo);;
	}

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
