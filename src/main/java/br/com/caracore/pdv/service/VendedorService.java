package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.VendedorRepository;
import br.com.caracore.pdv.repository.filter.VendedorFilter;

@Service
public class VendedorService {

	@Autowired
	VendedorRepository vendedorRepository;
	
	public List<Vendedor> pesquisar(VendedorFilter filtro) {
		List<Vendedor> lista = null;
		if (filtro.getNome() != null) {
			String nome = filtro.getNome();
			lista = vendedorRepository.findByNomeContainingIgnoreCase(nome);
		} else if (filtro.getLoja() != null) {
			Loja loja = filtro.getLoja();
			lista = vendedorRepository.findByLoja(loja);
		}
		return lista;
	}
}
