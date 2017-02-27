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
import br.com.caracore.pdv.service.exception.GerenteExistenteException;
import br.com.caracore.pdv.service.exception.UsuarioImpostadoException;
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
	
	public Vendedor verificarUsuario(Usuario usuario) {
		return vendedorRepository.findByUsuario(usuario);
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
		validarVendedor(vendedor);
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
	 * Método interno para validar vendedor
	 * 
	 * @param vendedor
	 */
	private void validarVendedor(Vendedor vendedor) {
		if (Util.validar(vendedor)) {
			if (Util.validar(vendedor.getLoja())) {
				Loja loja = vendedor.getLoja();
				if (Util.validar(vendedor.getTipo())) {
					if (vendedor.getTipo().equals(TipoVendedor.DEFAULT)) {
						Vendedor gerente = verificarGerente(loja);
						if (Util.validar(gerente)) {
							long codigo = vendedor.getCodigo().longValue();
							long codigoGerente = gerente.getCodigo().longValue();
							if (codigo != codigoGerente) {
								StringBuffer msg = mensagemErrorGerente(loja, gerente);
								throw new GerenteExistenteException(msg.toString());
							}
						}
					}
				}
			}
			if (Util.validar(vendedor.getUsuario())) {
				Usuario usuario = vendedor.getUsuario();
				Vendedor vendedorDB = verificarUsuario(usuario);
				if (Util.validar(vendedorDB)) {
					if (Util.validar(vendedor.getCodigo())) {
						long codigo = vendedor.getCodigo();
						long codigoDB = vendedorDB.getCodigo();
						if (codigo != codigoDB) {
							StringBuffer msg = new StringBuffer("Usuário já impostado!");
							throw new UsuarioImpostadoException(msg.toString());
						}
					}
				}
			}
		}
	}

	/**
	 * Método interno de mensagem de erro para o vendedor
	 * 
	 * @param loja
	 * @param gerente
	 * @return
	 */
	private StringBuffer mensagemErrorGerente(Loja loja, Vendedor gerente) {
		StringBuffer msg = new StringBuffer("Gerente ");
		msg.append(gerente.getNome().toUpperCase());
		msg.append(" da loja ");
		msg.append(loja.getNome().toUpperCase());
		msg.append(" já foi cadastrado!");
		return msg;
	}
	
}
