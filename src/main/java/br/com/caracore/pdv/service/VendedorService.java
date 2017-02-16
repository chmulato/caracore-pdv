package br.com.caracore.pdv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.TipoVendedor;
import br.com.caracore.pdv.repository.VendedorRepository;

@Service
public class VendedorService {

	@Autowired
	VendedorRepository vendedorRepository;
	
	public Vendedor buscarDefault() {
		Vendedor vendedor = null;
		TipoVendedor tipo = TipoVendedor.DEFAULT;
		vendedor = vendedorRepository.findByTipo(tipo);
		return vendedor;
	}
	
}
