package br.com.caracore.pdv.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.types.TipoOperador;

public interface OperadorRepository extends JpaRepository<Operador, Long> {
	
	public List<Operador> findByNomeContainingIgnoreCase(String nome);
	
    public Operador findByPerfil(TipoOperador perfil);
    
    public Operador findByNome(String nome);
    
    public List<Operador> findByEmail(String email);
    
}
