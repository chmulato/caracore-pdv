package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.types.TipoUsuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	public List<Usuario> findByNomeContainingIgnoreCase(String nome);
	
    public Usuario findByPerfil(TipoUsuario perfil);
    
}
