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
import br.com.caracore.pdv.util.Util;

@Service
public class EstoqueService {

	final private Date DATA_DE_HOJE = new Date();
	
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
			if ((Util.validar(estoque.getEstoqueMinimo())) && (Util.validar(estoque.getEstoqueMaximo()))) {
				int estoqueMinimo = estoque.getEstoqueMinimo().intValue();
				int estoqueMaximo = estoque.getEstoqueMaximo().intValue();
				if (estoqueMinimo > estoqueMaximo) {
					throw new QuantidadeMaximaInvalidaException("Quantidade máxima inválida!");
				}
			}
		}
		if (Util.validar(estoque.getProduto())) {
			Long codigoProduto = estoque.getProduto().getCodigo();
			produto = produtoRepository.findOne(codigoProduto);
			estoque.setValorUnitario(produto.getValor());
		}
	}
	
	/**
	 * Método esterno para salvar estoque informado
	 * 
	 * @param estoque
	 */
	public void salvar(Estoque estoque) {
		validarEstoque(estoque);
		estoqueRepository.save(estoque);
	}

	public void excluir(Long codigo) {
		estoqueRepository.delete(codigo);;
	}

	public Estoque pesquisarPorCodigo(Long codigo) {
		return estoqueRepository.findOne(codigo);
	}
	
	public List<Estoque> listarEstoque(Loja loja) {
		return estoqueRepository.findByLoja(loja);
	}
	
	public List<Estoque> listarEstoque(Produto produto) {
		return estoqueRepository.findByProduto(produto);
	}

	public List<Estoque> listarEstoque(Loja loja, Produto produto) {
		return estoqueRepository.findByLojaAndProduto(loja, produto);
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
			
			String dataInicial = null;
			String dataFinal = null;
			String strProduto = null;
			String strLoja = null;

			if ((!Util.validar(filtro.getDataInicial())) && (!Util.validar(filtro.getDataFinal())) && (!Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				Date data = DATA_DE_HOJE;
				
				Date dataI = Util.dataHoraInicialDoMes(data);
				Date dataF = Util.dataHoraFinalDoMes(data);
				
				lista = estoqueRepository.findByDataBetweenByOrderByDataDesc(dataI, dataF);
			}
			
			if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (!Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				lista = estoqueRepository.findByDataBetweenByOrderByDataDesc(dataI, dataF);
			}
			
			if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				strLoja = filtro.getLoja();

				Loja loja = lojaRepository.findByNomeIgnoreCase(strLoja);
				
				if (Util.validar(loja)) {
					lista = estoqueRepository.findByDataBetweenAndLojaByOrderByDataDesc(dataI, dataF, loja);
				}
			}

			if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (!Util.validar(filtro.getLoja())) && (Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				strProduto = filtro.getProduto();

				Produto produto = produtoRepository.findByDescricaoIgnoreCase(strProduto);
				
				if (Util.validar(produto)) {
					lista = estoqueRepository.findByDataBetweenAndProdutoByOrderByDataDesc(dataI, dataF, produto);
				}
			}
			
			if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (Util.validar(filtro.getLoja())) && (Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				strLoja = filtro.getLoja();
				strProduto = filtro.getProduto();

				Loja loja = lojaRepository.findByNomeIgnoreCase(strLoja);
				Produto produto = produtoRepository.findByDescricaoIgnoreCase(strProduto);
				
				if ((Util.validar(loja)) && (Util.validar(produto))) {
					lista = estoqueRepository.findByDataBetweenAndLojaAndProdutoByOrderByDataDesc(dataI, dataF, loja, produto);
				}
			}
		
		}
		
		return lista;
	}
}
