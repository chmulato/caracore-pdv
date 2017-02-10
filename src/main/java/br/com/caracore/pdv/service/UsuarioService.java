package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.repository.UsuarioRepository;

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
	
	public List<Usuario> buscarTodos() {
		return usuarioRepository.findAll();
	}
	
	public Usuario salvar(Usuario usuario) {
		String login = usuario.getNome();
		String email = usuario.getEmail();
		String repetirEmail = usuario.getRepetirEmail();
		String senha = usuario.getSenha();
		String repetirSenha = usuario.getRepetirSenha();
		if (verificarLogin(login)) {
			throw new IllegalArgumentException("Login já existente!");
		}
		if (repetirSenha != null && !repetirSenha.equals(senha)) {
			throw new IllegalArgumentException("Senha inválida!");
		}
		if (repetirEmail != null && !repetirEmail.equals(email)) {
			throw new IllegalArgumentException("Email inválido!");
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
