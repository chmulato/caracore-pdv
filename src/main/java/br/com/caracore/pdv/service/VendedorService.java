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
import br.com.caracore.pdv.util.Util;

@Service
public class VendedorService {

	@Autowired
	LojaRepository lojaRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	VendedorRepository vendedorRepository;
	
	/**
	 * Método externo verificar o gerente da loja
	 * 
	 * @param loja
	 * @return
	 */
	public Vendedor verificarGerente(Loja loja) {
		return buscarDefault(loja);
	}
	
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
		if (Util.validar(vendedor)) {
			if (Util.validar(vendedor.getLoja())) {
				Loja loja = vendedor.getLoja();
				Vendedor gerente = verificarGerente(loja);
				if (Util.validar(gerente)) {
					StringBuffer msg = mensagemErro(loja, gerente);
					throw new IllegalArgumentException(msg.toString());
				}
			}
		}
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

	/**
	 * Método interno de mensagem de erro para o vendedor
	 * 
	 * @param loja
	 * @param gerente
	 * @return
	 */
	private StringBuffer mensagemErro(Loja loja, Vendedor gerente) {
		StringBuffer msg = new StringBuffer("Gerente ");
		msg.append(gerente.getNome().toUpperCase());
		msg.append(" da loja ");
		msg.append(loja.getNome().toUpperCase());
		msg.append(" já foi cadastrado!");
		return msg;
	}
	
}
