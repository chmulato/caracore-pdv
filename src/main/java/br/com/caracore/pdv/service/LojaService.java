package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.LojaRepository;
import br.com.caracore.pdv.repository.VendedorRepository;
import br.com.caracore.pdv.repository.filter.LojaFilter;

@Service
public class LojaService {

	@Autowired
	private LojaRepository lojaRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	public void salvar(Loja loja) {
		lojaRepository.save(loja);
	}

	public void excluir(Long codigo) {
		lojaRepository.delete(codigo);;
	}

	public Loja pesquisarPorCodigo(Long codigo) {
		return lojaRepository.findOne(codigo);
	}

	public List<Loja> pesquisar(LojaFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return lojaRepository.findByNomeContainingIgnoreCase(nome);
	}

	
	public List<Vendedor> listarVendedores(Loja loja) {
		return vendedorRepository.findByLoja(loja);
	}
	
}
