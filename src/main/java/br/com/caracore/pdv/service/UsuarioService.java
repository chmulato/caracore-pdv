package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.types.TipoUsuario;
import br.com.caracore.pdv.repository.UsuarioRepository;
import br.com.caracore.pdv.repository.filter.UsuarioFilter;
import br.com.caracore.pdv.service.exception.AdminExistenteException;
import br.com.caracore.pdv.service.exception.EmailInvalidoException;
import br.com.caracore.pdv.service.exception.LoginExistenteException;
import br.com.caracore.pdv.service.exception.SenhaInvalidaException;
import br.com.caracore.pdv.util.Util;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private Usuario retornarUsuario(List<Usuario> listar) {
		Usuario result = null;
		if (listar != null && listar.size() == 1) {
			for(Usuario usuario : listar) {
				result = usuario;
			}
		}
		return result;
	}
	
	private Usuario buscarAdministrador() {
		Usuario usuarioAdmin = null;
		TipoUsuario perfil = TipoUsuario.ADMINISTRADOR;
		usuarioAdmin = usuarioRepository.findByPerfil(perfil);
		return usuarioAdmin;
	}
	
	private boolean verificarLogin(String login) {
		boolean result = false;
		if (login != null && !login.equals("")) {
			List<Usuario> listar = pesquisarPorLogin(login);
			Usuario usuario = retornarUsuario(listar);
			if (usuario != null) {
				result = true;
			}
		}
		return result;
	}
	
	public Usuario pesquisarPorNome(String login) {
		Usuario result = null;
		List<Usuario> lista = pesquisarPorLogin(login);
		if (lista != null && lista.size() > 0) {
			if (lista.size() == 1) {
				for (Usuario usuario : lista) {
					result = usuario;
				}
			}
		}
		return result;
	}

	public List<Usuario> pesquisar(UsuarioFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return usuarioRepository.findByNomeContainingIgnoreCase(nome);
	}
	
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
	
	public Usuario salvar(Usuario usuario) {
		boolean verificar = false;
		String login = usuario.getNome();
		String email = usuario.getEmail();
		String repetirEmail = usuario.getRepetirEmail();
		String senha = usuario.getSenha();
		String repetirSenha = usuario.getRepetirSenha();
		Usuario admin = buscarAdministrador();
		if (Util.validar(admin)) {
			throw new AdminExistenteException("Administrador já existente!");
		}
		verificar = verificarLogin(login);
		if (verificar) {
			throw new LoginExistenteException("Login já existente!");
		}
		if (!repetirSenha.equals(senha)) {
			throw new SenhaInvalidaException("Senha inválida!");
		}
		if (!repetirEmail.equals(email)) {
			throw new EmailInvalidoException("Email inválido!");
		}
		return usuarioRepository.save(usuario);
	}
	
	public void excluirPorLogin(String login) {
		List<Usuario> listar = pesquisarPorLogin(login);
		Usuario usuario = retornarUsuario(listar);
		excluir(usuario.getCodigo());
	}

	public List<Usuario> pesquisarPorLogin(String login) {
		return usuarioRepository.findByNomeContainingIgnoreCase(login);
	}

	public void excluir(Long codigo) {
		usuarioRepository.delete(codigo);
	}

	public Usuario pesquisarPorId(Long codigo) {
		return usuarioRepository.findOne(codigo);
	}
}
