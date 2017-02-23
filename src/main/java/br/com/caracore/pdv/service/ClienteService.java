package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.repository.ClienteRepository;
import br.com.caracore.pdv.repository.filter.ClienteFilter;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public void salvar(Cliente cliente) {
		clienteRepository.save(cliente);
	}

	public void excluir(Long codigo) {
		clienteRepository.delete(codigo);;
	}

	public Cliente pesquisarPorCodigo(Long codigo) {
		return clienteRepository.findOne(codigo);
	}

	public List<Cliente> pesquisar(ClienteFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return clienteRepository.findByNomeContainingIgnoreCase(nome);
	}

}
