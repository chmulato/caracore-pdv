package br.com.caracore.pdv.service;

import java.math.BigDecimal;
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
	 * Método externo para calcular o total do estoque
	 * 
	 * @param estoques
	 * @return
	 */
	public BigDecimal totalEmEstoque(List<Estoque> estoques) {
		double total = 0.0d;
		if (Util.validar(estoques)) {
			double soma = 0.0d;
			for (Estoque estoque : estoques) {
				if ((Util.validar(estoque.getQuantidade())) && (Util.validar(estoque.getValorUnitario()))) {
					int quantidade = estoque.getQuantidade().intValue();
					double valorUnitario = estoque.getValorUnitario().doubleValue();
					soma = quantidade * valorUnitario;
				}
				total = soma + total;
			}
		}
		return BigDecimal.valueOf(total);
	}

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
			List<Estoque> estoqueDB = estoqueRepository.findByLojaAndProduto(loja, produto);
			if (Util.validar(estoqueDB)) {
				for (Estoque _estoque : estoqueDB) {
					estoque = _estoque;
					break;
				}
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
			
			} else if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (!Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				lista = estoqueRepository.findByDataBetweenByOrderByDataDesc(dataI, dataF);
			
			} else if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (Util.validar(filtro.getLoja())) && (!Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				strLoja = FILTRO_LIKE +  filtro.getLoja() + FILTRO_LIKE;
				
				lista = estoqueRepository.findByDataBetweenAndLojaByOrderByDataDesc(dataI, dataF, strLoja);
			
			} else if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (!Util.validar(filtro.getLoja())) && (Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				strProduto = FILTRO_LIKE + filtro.getProduto() + FILTRO_LIKE;
				
				lista = estoqueRepository.findByDataBetweenAndProdutoByOrderByDataDesc(dataI, dataF, strProduto);

			} else if ((Util.validar(filtro.getDataInicial())) && (Util.validar(filtro.getDataFinal())) && (Util.validar(filtro.getLoja())) && (Util.validar(filtro.getProduto()))) {
				
				dataInicial = filtro.getDataInicial();
				dataFinal = filtro.getDataFinal();
				
				Date dataI = Util.formataData(dataInicial);
				Date dataF = Util.formataData(dataFinal);
				dataI = Util.dataHoraInicial(dataI);
				dataF = Util.dataHoraFinal(dataF);
				
				strLoja = FILTRO_LIKE + filtro.getLoja() + FILTRO_LIKE;
				strProduto = FILTRO_LIKE + filtro.getProduto() + FILTRO_LIKE;

				lista = estoqueRepository.findByDataBetweenAndLojaAndProdutoByOrderByDataDesc(dataI, dataF, strLoja, strProduto);
			
			}
		
		}
		
		return lista;
	}
}
