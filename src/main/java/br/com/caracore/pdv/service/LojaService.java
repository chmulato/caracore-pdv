package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.VendedorRepository;

@Service
public class LojaService {

	@Autowired
	private VendedorRepository vendedorRepository;
	
	public List<Vendedor> listarVendedores(Loja loja) {
		return vendedorRepository.findByLoja(loja);
	}
	
}
