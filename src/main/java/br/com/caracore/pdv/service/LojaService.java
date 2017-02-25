package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.LojaRepository;
import br.com.caracore.pdv.repository.VendedorRepository;
import br.com.caracore.pdv.repository.filter.LojaFilter;
import br.com.caracore.pdv.service.exception.NomeExistenteException;
import br.com.caracore.pdv.util.Util;

@Service
public class LojaService {

	@Autowired
	private LojaRepository lojaRepository;

	@Autowired
	private VendedorRepository vendedorRepository;

	/**
	 * Método interno de validar nome da loja
	 * 
	 * @param loja
	 */
	private void validarNome(Loja loja) {
		if ((Util.validar(loja)) && Util.validar(loja.getNome())) {
			String nome = loja.getNome();
			Loja lojaDB = pesquisarPorNome(nome);
			if (Util.validar(lojaDB)) {
				if (Util.validar(loja.getCodigo())) {
					long codigo = loja.getCodigo().longValue();
					long codigoDB = lojaDB.getCodigo().longValue();
					if (codigo != codigoDB) {
						throw new NomeExistenteException("Loja já existente!");
					}
				} else {
					throw new NomeExistenteException("Loja já existente!");
				}
			}
		}
	}

	/**
	 * Método interno para recuperar loja por nome
	 * 
	 * @param nome
	 * @return
	 */
	private Loja pesquisarPorNome(String nome) {
		return lojaRepository.findByNomeIgnoreCase(nome);
	}

	public void salvar(Loja loja) {
		validarNome(loja);
		lojaRepository.save(loja);
	}

	public void excluir(Long codigo) {
		lojaRepository.delete(codigo);;
	}

	public Loja pesquisarPorCodigo(Long codigo) {
		return lojaRepository.findOne(codigo);
	}

	public List<Loja> pesquisar(LojaFilter filtro) {
		String nome = filtro.getNome() == null ? "%" : filtro.getNome();
		return lojaRepository.findByNomeContainingIgnoreCase(nome);
	}
	
	public List<Vendedor> listarVendedores(Loja loja) {
		return vendedorRepository.findByLoja(loja);
	}
	
}
