package br.com.caracore.pdv.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.repository.ProdutoRepository;
import br.com.caracore.pdv.repository.filter.ProdutoFilter;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;

	public void salvar(Produto produto) {
		produtoRepository.save(produto);
	}

	public void excluir(Long codigo) {
		produtoRepository.delete(codigo);;
	}

	public Produto pesquisarPorId(Long codigo) {
		return produtoRepository.findOne(codigo);
	}

	public List<Produto> pesquisar(ProdutoFilter filtro) {
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return produtoRepository.findByDescricaoContainingIgnoreCase(descricao);
	}
}
