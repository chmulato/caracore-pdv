package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.TipoVendedor;
import br.com.caracore.pdv.repository.LojaRepository;
import br.com.caracore.pdv.repository.UsuarioRepository;
import br.com.caracore.pdv.repository.VendedorRepository;
import br.com.caracore.pdv.repository.filter.VendedorFilter;

@Service
public class VendedorService {

	@Autowired
	LojaRepository lojaRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	VendedorRepository vendedorRepository;
	
	public List<Usuario> buscarUsuarios() {
		List<Usuario> lista = null;
		lista = usuarioRepository.findAll();
		return lista;
	}
	
	public Vendedor buscarPorUsuario(Usuario usuario) {
		Vendedor vendedor = null;
		vendedor = vendedorRepository.findByUsuario(usuario);
		return vendedor;
	}
	
	
	public List<Loja> buscarLojas() {
		List<Loja> lista = null;
		lista = lojaRepository.findAll();
		return lista;
	}
	
	public Vendedor buscarDefault(Loja loja) {
		Vendedor vendedor = null;
		TipoVendedor tipo = TipoVendedor.DEFAULT;
		vendedor = vendedorRepository.findByTipoAndLoja(tipo, loja);
		return vendedor;
	}

	public void salvar(Vendedor vendedor) {
		vendedorRepository.save(vendedor);
	}

	public void excluir(Long codigo) {
		vendedorRepository.delete(codigo);;
	}

	public Vendedor pesquisarPorId(Long codigo) {
		return vendedorRepository.findOne(codigo);
	}

	public List<Vendedor> pesquisar(VendedorFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return vendedorRepository.findByNomeContainingIgnoreCase(nome);
	}
	
}
