package br.com.caracore.pdv.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Estoque;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.repository.EstoqueRepository;
import br.com.caracore.pdv.repository.LojaRepository;
import br.com.caracore.pdv.repository.ProdutoRepository;
import br.com.caracore.pdv.repository.filter.EstoqueFilter;
import br.com.caracore.pdv.service.exception.LojaNaoCadastradaException;
import br.com.caracore.pdv.service.exception.ProdutoNaoCadastradoException;
import br.com.caracore.pdv.service.exception.QuantidadeInvalidaException;
import br.com.caracore.pdv.service.exception.QuantidadeMaximaInvalidaException;
import br.com.caracore.pdv.service.exception.QuantidadeMinimaInvalidaException;
import br.com.caracore.pdv.util.Util;

@Service
public class EstoqueService {

	final private String FILTRO_LIKE = "%";
	
	final private Date DATA_DE_HOJE = new Date();

	final private Boolean ESTOQUE_ATUALIZADO = Boolean.TRUE;
	
	@Autowired
	private EstoqueRepository estoqueRepository;

	@Autowired
	private LojaRepository lojaRepository;

	@Autowired
	private ProdutoRepository produtoRepository;

	
	/**
	 * Método externo para recuperar lista de lojas cadastradas
	 * 
	 * @return
	 */
	public List<Loja> listaLojas() {
		List<Loja> lista = lojaRepository.findAll();
		if (!Util.validar(lista)) {
			lista = Util.criarListaDeLojas();
		}
		return lista;
	}

	/**
	 * Método externo para recuperar lista de produtos cadastrados
	 * 
	 * @return
	 */
	public List<Produto> listaProdutos() {
		List<Produto> lista = produtoRepository.findAll();
		if (!Util.validar(lista)) {
			lista = Util.criarListaDeProdutos();
		}
		return lista;
	}

	/**
	 * Método interno para validar estoque 
	 * 
	 * @param estoque
	 */
	private void validarEstoque(Estoque estoque) {
		Produto produto = null;
		if (Util.validar(estoque)) {
			if (!Util.validar(estoque.getLoja())) {
				throw new LojaNaoCadastradaException("Loja não cadastrada!");
			}
			if (!Util.validar(estoque.getProduto())) {
				throw new ProdutoNaoCadastradoException("Produto não cadastrado!");
			}
			if (!Util.validar(estoque.getQuantidade())) {
				throw new QuantidadeInvalidaException("Quantidade não cadastrada!");
			}
			if (!Util.validar(estoque.getEstoqueMinimo())) {
				throw new QuantidadeInvalidaException("Estoque mínimo não cadastrado!");
			}
			if ((Util.validar(estoque.getQuantidade())) && (Util.validar(estoque.getEstoqueMinimo()))) {
				int quantidade = estoque.getQuantidade().intValue();
				int estoqueMinimo = estoque.getEstoqueMinimo().intValue();
				if (quantidade < estoqueMinimo) {
					throw new QuantidadeMinimaInvalidaException("Quantidade mínima inválida!");
				}
			}
			if ((Util.validar(estoque.getEstoqueMinimo())) && (Util.validar(estoque.getEstoqueMaximo()))) {
				int estoqueMinimo = estoque.getEstoqueMinimo().intValue();
				int estoqueMaximo = estoque.getEstoqueMaximo().intValue();
				if (estoqueMaximo < estoqueMinimo ) {
					throw new QuantidadeMaximaInvalidaException("Quantidade máxima inválida!");
				}
			}
		}
		if (Util.validar(estoque.getProduto())) {
			Long codigoProduto = estoque.getProduto().getCodigo();
			produto = produtoRepository.findOne(codigoProduto);
			estoque.setValorUnitario(produto.getValor());
			estoque.setData(DATA_DE_HOJE);
		}
	}

	/**
	 * Método interno para recuperar estoque por produto e loja
	 * 
	 * @param loja
	 * @param produto
	 * @return
	 */
	private Estoque buscarProdutoEmEstoque(Loja loja, Produto produto) {
		Estoque estoque = null;
		if ((Util.validar(loja)) && (Util.validar(produto))) {
			Estoque estoqueDB = estoqueRepository.findByLojaAndProduto(loja, produto);
			if (Util.validar(estoqueDB)) {
				estoque = estoqueDB;
			}
		}
		return estoque;
	}
	
	/**
	 * Método esterno para salvar ou atualizar estoque informado
	 * 
	 * @param estoque
	 * @return
	 */
	public Estoque salvar(Estoque estoque) {
		boolean atualizado = false;
		validarEstoque(estoque);
		Loja loja = estoque.getLoja();
		Produto produto = estoque.getProduto();
		Estoque estoqueDB = buscarProdutoEmEstoque(loja, produto);
		if (Util.validar(estoqueDB) &&(Util.validar(estoqueDB.getCodigo()))) {
			estoque.setCodigo(estoqueDB.getCodigo());
			atualizado = ESTOQUE_ATUALIZADO.booleanValue();
		}
		estoqueRepository.save(estoque);
		if (atualizado) {
			estoque.setSituacao(ESTOQUE_ATUALIZADO);
		}
		return estoque;
	}

	public void excluir(Long codigo) {
		estoqueRepository.delete(codigo);;
	}

	public Estoque pesquisarPorCodigo(Long codigo) {
		return estoqueRepository.findOne(codigo);
	}

	/**
	 * Método para pesquisar o estoque por filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	public List<Estoque> pesquisar(EstoqueFilter filtro) {
		
		List<Estoque> lista = null;
		
		if (Util.validar(filtro)) {
			
			Long codigo = null;
			String strCodigoBarra = null;
			String strProduto = null;
			String strLoja = null;

			if ((!Util.validar(filtro.getCodigo())) && (!Util.validar(filtro.getCodigoBarra())) && (!Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				lista = estoqueRepository.findAll();
			
			} else if ((!Util.validar(filtro.getCodigo())) && (!Util.validar(filtro.getCodigoBarra())) && (Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				strLoja = FILTRO_LIKE +  filtro.getLoja() + FILTRO_LIKE;
				
				lista = estoqueRepository.findByLojaByOrderByProduto(strLoja);
			
			} else if ((!Util.validar(filtro.getCodigo())) && (!Util.validar(filtro.getCodigoBarra())) && (!Util.validar(filtro.getLoja())) && (Util.validar(filtro.getProduto()))) {
				
				strProduto = FILTRO_LIKE + filtro.getProduto() + FILTRO_LIKE;
				
				lista = estoqueRepository.findByProdutoByOrderByProduto(strProduto);

			} else if ((!Util.validar(filtro.getCodigo())) && (!Util.validar(filtro.getCodigoBarra())) && (Util.validar(filtro.getLoja())) && (Util.validar(filtro.getProduto()))) {
				
				strLoja = FILTRO_LIKE + filtro.getLoja() + FILTRO_LIKE;
				strProduto = FILTRO_LIKE + filtro.getProduto() + FILTRO_LIKE;

				lista = estoqueRepository.findByLojaAndProdutoByOrderByProduto(strLoja, strProduto);
			
			} else if ((Util.validar(filtro.getCodigo())) && (!Util.validar(filtro.getCodigoBarra())) && (!Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {

				codigo = filtro.getCodigo();
				Produto produto = produtoRepository.findOne(codigo);
				
				if (Util.validar(produto)) {
					lista = estoqueRepository.findByProdutoByOrderByProduto(produto);
				}
				
			} else if ((!Util.validar(filtro.getCodigo())) && (Util.validar(filtro.getCodigoBarra())) && (!Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {

				strCodigoBarra = filtro.getCodigoBarra();
				Produto produto = produtoRepository.findByCodigoBarra(strCodigoBarra);
				
				if (Util.validar(produto)) {
					lista = estoqueRepository.findByProdutoByOrderByProduto(produto);
				}
				
			} else if ((Util.validar(filtro.getCodigo())) && (!Util.validar(filtro.getCodigoBarra())) && (Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {

				strLoja = FILTRO_LIKE + filtro.getLoja() + FILTRO_LIKE;
				codigo = filtro.getCodigo();
				Produto produto = produtoRepository.findOne(codigo);
				
				if (Util.validar(produto)) {
					lista = estoqueRepository.findByLojaAndProdutoByOrderByProduto(strLoja, produto);
				}
				
			} else if ((!Util.validar(filtro.getCodigo())) && (Util.validar(filtro.getCodigoBarra())) && (Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {

				strLoja = FILTRO_LIKE + filtro.getLoja() + FILTRO_LIKE;
				strCodigoBarra = filtro.getCodigoBarra();
				Produto produto = produtoRepository.findByCodigoBarra(strCodigoBarra);
				
				if (Util.validar(produto)) {
					lista = estoqueRepository.findByLojaAndProdutoByOrderByProduto(strLoja, produto);
				}
				
			}
		
		}
		
		return lista;
	}
}
