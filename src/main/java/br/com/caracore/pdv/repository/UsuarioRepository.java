package br.com.caracore.pdv.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
