package br.com.caracore.pdv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.VendedorRepository;

@Service
public class VendedorService {

	@Autowired
	VendedorRepository vendedorRepository;
	
	public Vendedor buscarPorUsuario(Usuario usuario) {
		Vendedor vendedor = null;
		Long usuarioId = usuario.getCodigo();
		vendedor = vendedorRepository.findByUsuarioId(usuarioId);
		return vendedor;
	}
}
