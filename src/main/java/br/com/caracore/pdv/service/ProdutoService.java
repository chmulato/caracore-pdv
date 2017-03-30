package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.repository.ProdutoRepository;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;
import br.com.caracore.pdv.service.exception.CodigoBarraExistenteException;
import br.com.caracore.pdv.service.exception.CodigoExistenteException;
import br.com.caracore.pdv.service.exception.ProdutoExistenteException;
import br.com.caracore.pdv.service.exception.UnidadeInvalidaException;
import br.com.caracore.pdv.util.Util;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	/**
	 * Método interno para validar nome do produto
	 * 
	 * @param produto
	 */
	private void validarProduto(Produto produto) {
		boolean valida = false;
		valida = pesquisarDescricao(produto);
		if (valida) {
			throw new ProdutoExistenteException("Produto existente!");
		}
		valida = false;
		valida = pesquisarCodigoBarra(produto);
		if (valida) {
			throw new CodigoBarraExistenteException("Código de barra existente!");
		}
		if (!Util.validar(produto.getUnidade())) {
			throw new UnidadeInvalidaException("Unidade inválida!!");
		}
	}

	public void salvar(Produto produto) {
		validarProduto(produto);
		try {
			produtoRepository.save(produto);
		} catch (DataIntegrityViolationException e) {
			throw new CodigoExistenteException("Código do produto existente!");
		}
	}

	public void excluir(Long codigo) {
		produtoRepository.delete(codigo);;
	}

	public Produto pesquisarPorCodigoBarra(String codigoBarra) {
		return produtoRepository.findByCodigoBarra(codigoBarra);
	}

	public Produto pesquisarPorCodigo(Long codigo) {
		return produtoRepository.findOne(codigo);
	}

	public List<Produto> pesquisar(ProdutoFilter filtro) {
		List<Produto> lista = null;
		if (Util.validar(filtro.getDescricao())) {
			String descricao = filtro.getDescricao();
			lista = produtoRepository.findByDescricaoContainingIgnoreCase(descricao);
		} else if (Util.validar(filtro.getCodigoBarra())) {
			String codigoBarra = filtro.getCodigoBarra(); 
			lista = produtoRepository.findByCodigoBarraContainingIgnoreCase(codigoBarra);
		} else {
			lista = produtoRepository.findByDescricaoContainingIgnoreCase("%");
		}
		return lista;
	}
	
	private boolean pesquisarCodigoBarra(Produto produto) {
		boolean existe = false;
		if (Util.validar(produto) && Util.validar(produto.getCodigoBarra())) {
			String codigoBarra = produto.getCodigoBarra();
			Produto produtoDB = produtoRepository.findByCodigoBarraIgnoreCase(codigoBarra);
			if (Util.validar(produtoDB)) {
				long codigo = produto.getCodigo().longValue();
				long codigoDB = produtoDB.getCodigo().longValue();
				if (codigo != codigoDB) {
					existe = true;
				}
			}
		}
		return existe;
	}
	
	private boolean pesquisarDescricao(Produto produto) {
		boolean existe = false;
		if (Util.validar(produto) && Util.validar(produto.getDescricao())) {
			String descricao = produto.getDescricao();
			Produto produtoDB = produtoRepository.findByDescricaoIgnoreCase(descricao);
			if (Util.validar(produtoDB)) {
				long codigo = produto.getCodigo().longValue();
				long codigoDB = produtoDB.getCodigo().longValue();
				if (codigo != codigoDB) {
					existe = true;
				}
			}
		}
		return existe;
	}
}
