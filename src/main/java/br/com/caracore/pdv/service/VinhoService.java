package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Vinho;
import br.com.caracore.pdv.repository.VinhoRepository;
import br.com.caracore.pdv.repository.filter.VinhoFilter;

@Service
public class VinhoService {

	@Autowired
	private VinhoRepository vinhoRepository;

	public List<Vinho> pesquisar(VinhoFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return vinhoRepository.findByNomeContainingIgnoreCase(nome);
	}

	public void salvar(Vinho vinho) {
		vinhoRepository.save(vinho);
	}

	public void excluir(Long codigo) {
		vinhoRepository.delete(codigo);;
	}

	public Vinho pesquisarPorId(Long codigo) {
		return vinhoRepository.findOne(codigo);
	}
}
