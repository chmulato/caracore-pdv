package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.repository.ProdutoRepository;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.service.exception.CodigoExistenteException;
import br.com.caracore.pdv.service.exception.ProdutoExistenteException;
import br.com.caracore.pdv.util.Util;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public void salvar(Produto produto) {
		boolean valida = pesquisarDescricao(produto);
		if (valida) {
			throw new ProdutoExistenteException("Produto existente!");
		}
		try {
			produtoRepository.save(produto);
		} catch (DataIntegrityViolationException e) {
			throw new CodigoExistenteException("Código do produto existente!");
		}
	}

	public void excluir(Long codigo) {
		produtoRepository.delete(codigo);;
	}

	public Produto pesquisarPorCodigo(Long codigo) {
		return produtoRepository.findOne(codigo);
	}

	public List<Produto> pesquisar(ProdutoFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return produtoRepository.findByDescricaoContainingIgnoreCase(descricao);
	}
	
	private boolean pesquisarDescricao(Produto produto) {
		boolean existe = false;
		if (Util.validar(produto) && Util.validar(produto.getDescricao())) {
			String descricao = produto.getDescricao();
			Produto produtoExistente = produtoRepository.findByDescricao(descricao);
			if (Util.validar(produtoExistente)) {
				if (!produto.getCodigo().equals(produtoExistente.getCodigo())) {
					existe = true;
				}
			}
		}
		return existe;
	}
}
