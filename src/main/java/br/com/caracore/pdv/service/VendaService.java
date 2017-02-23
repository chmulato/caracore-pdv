package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.model.types.StatusVenda;
import br.com.caracore.pdv.repository.VendaRepository;
import br.com.caracore.pdv.util.Util;

@Service
public class VendaService {

	final private Date DATA_DE_HOJE = new Date();
	
	final private int QUANTIDADE_UNITARIA = 1;

	@Autowired
	private VendaRepository vendaRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private LojaService lojaService;

	@Autowired
	private VendedorService vendedorService;

	@Autowired
	private ItemVendaService itemVendaService;
	
	/**
	 * Metodo para recuperar venda em aberto informando o vendedor
	 * 
	 * @param vendedor
	 * @return
	 */
	public Venda recuperarVendaEmAberto(Vendedor vendedor) {
		Venda result = new Venda();
		List<Venda> lista = vendaRepository.findByVendedorAndDataAndStatus(vendedor, DATA_DE_HOJE, StatusVenda.EM_ABERTO);
		if (Util.validar(lista)) {
			if (lista.size() == QUANTIDADE_UNITARIA) {
				for (Venda venda : lista) {
					if (!Util.validar(venda.getItens())) {
						List<ItemVenda> itens = itemVendaService.buscarItens(venda);
						if (Util.validar(itens)) {
							venda.setItens(itens);
						}
					}
					venda = totalizar(venda);
					result = venda;
				}
			}
		}
		return result;
	}
	
	/**
	 * Método para recuperar o vendedor do usuário, 
	 * se não encontrar recupera o vendedor padrão da loja
	 * 
	 * @param usuario
	 * @return
	 */
	public Vendedor buscarVendedor(Usuario usuario) {
		Vendedor vendedor = null;
		if (Util.validar(usuario)) {
			vendedor = vendedorService.buscarPorUsuario(usuario);
			if (!Util.validar(vendedor)) {
				if (Util.validar(usuario.getLoja())) {
					Loja loja = usuario.getLoja();
					vendedor = vendedorService.buscarDefault(loja);
				}
			}
		}
		return vendedor;
	}
	
	/**
	 * Método interno para recuperar lista de vendedores da loja
	 * 
	 * @param loja
	 * @return
	 */
	private List<Vendedor> listarVendedoresPorLoja(Loja loja) {
		return lojaService.listarVendedores(loja);
	}

	/**
	 * Método externo para recuperar usuario logado
	 * 
	 * @param login
	 * @return
	 */
	public Usuario recuperarUsuario(String login) {
		Usuario usuario = null;
		if (Util.validar(login)) {
			usuario = usuarioService.pesquisarPorNome(login); 
		}
		return usuario;
	}
	
	/**
	 *	Método para validar se existe um venda com itens de compra
	 * 
	 * @param venda
	 * @return
	 */
	public boolean validarVendaEmAndamento(Venda venda) {
		boolean validar = false;
		if (Util.validar(venda)) {
			if (Util.validar(venda.getCodigo())) {
				if (Util.validar(venda.getItens())) {
					validar = true;
				}
			}
		}
		return validar;
	}

	/**
	 * Método externo para salvar vendas. Salva a data atualizada de hoje 
	 * 
	 * @param venda
	 * @return
	 */
	public Venda salvar(Venda venda) {
		if (Util.validar(venda)) {
			venda.setData(DATA_DE_HOJE);
		}
		venda = vendaRepository.save(venda);
		return venda;
	}

	/**
	 * Método externo para excluir uma venda
	 * 
	 * @param codigo
	 */
	public void excluir(Long codigo) {
		vendaRepository.delete(codigo);
	}

	/**
	 * Método externo para pesquisar uma venda
	 * 
	 * @param codigo
	 * @return
	 */
	public Venda pesquisarPorId(Long codigo) {
		return vendaRepository.findOne(codigo);
	}
	
	/**
	 * Método para recuperar lista de vendedores da loja informando o usuário da loja.
	 * No caso de nenhum usuário for encontrado recupera vendedor gerente
	 * 
	 * @param usuario
	 * @return
	 */
	public List<Vendedor> listarVendedoresPorUsuario(Usuario usuario) {
		List<Vendedor> lista = null;
		if (Util.validar(usuario) && Util.validar(usuario.getLoja())) {
			Loja loja = usuario.getLoja();
			Vendedor vendedor = vendedorService.buscarDefault(loja);
			if (Util.validar(vendedor)) {
				lista = listarVendedoresPorLoja(loja);
			}
		}
		return lista;
	}

	/**
	 * Método Interno para calcular o subTotal do item
	 * 
	 * @param item
	 * @return
	 */
	private BigDecimal subTotal(ItemVenda item) {
		long lngSubTotal = 0;
		if (Util.validar(item)) {
			if (Util.validar(item.getPrecoUnitario()) 
					&& (Util.validar(item.getDesconto())) 
					&& (Util.validar(item.getQuantidade()))) {
				long lngPrecoTotal = item.getPrecoUnitario().longValue();
				long lngDesconto = item.getDesconto().longValue();
				int intQuantidade = item.getQuantidade().intValue();
				if (lngDesconto >= 0 && lngDesconto <= 1) {
					lngPrecoTotal = lngPrecoTotal * intQuantidade;
					lngSubTotal = lngPrecoTotal - (lngPrecoTotal * lngDesconto);
				}
			}
		}
		return BigDecimal.valueOf(lngSubTotal);
	}
	
	/**
	 * Metodo interno para adicionar item de produto na cesta de compras
	 * 
	 * @param codigoProduto
	 * @return
	 */
	private ItemVenda carregarItem(Long codigoProduto) {
		ItemVenda item = new ItemVenda();
		if (Util.validar(codigoProduto)) {
			Produto produto = produtoService.pesquisarPorId(codigoProduto);
			if (Util.validar(produto)) {
				item.setDesconto(BigDecimal.ZERO);
				item.setProduto(produto);
				item.setPrecoUnitario(produto.getValor());
				item.setQuantidade(Integer.valueOf(QUANTIDADE_UNITARIA));
				item.setSubTotal(subTotal(item));
			}
		}
		return item;
	}
	
	/**
	 * Metodo externo para carregar os itens de venda na cesta
	 * 
	 * @param codigoProduto
	 * @param usuario
	 * @return
	 */
	public Venda comprar(Long codigoProduto, Usuario usuario) {
		Venda result = null;
		ItemVenda novoItem = carregarItem(codigoProduto);
		if (Util.validar(usuario)) {
			Vendedor vendedor = buscarVendedor(usuario);
			if (Util.validar(vendedor)) {
				Venda venda = recuperarVendaEmAberto(vendedor);
				if (Util.validar(venda)) {

					List<ItemVenda> itens = new ArrayList<>();
					
					// setar valores defaults
					venda.setVendedor(vendedor);
					venda.setDescontoTotal(BigDecimal.ZERO);
					venda.setStatus(StatusVenda.EM_ABERTO);
					
					if (Util.validar(venda.getItens())) {
						itens = venda.getItens();
						itens.add(novoItem);
					} else {
						itens.add(novoItem);
					}
					
					venda.setItens(itens);
					venda = salvar(venda);
					
					if (Util.validar(venda.getCodigo())) {
						itens = itemVendaService.buscarItens(venda);
						itens.add(novoItem);
						itens = itemVendaService.salvarItens(itens, venda);
						if (Util.validar(itens)) {
							venda.setItens(itens);
						}
					}
				}
				result = venda;
			}
		}
		return result;
	}

	
	/**
	 * Método interno para totalizar valores da compra
	 * 
	 * @param venda
	 * @return
	 */
	private Venda totalizar(Venda venda) {
		if (Util.validar(venda)) {
			long lngTotal = 0;
			if (Util.validar(venda.getItens())) {
				long total = 0L;
				for (ItemVenda item : venda.getItens()) {
					BigDecimal subTotal = subTotal(item);
					item.setSubTotal(subTotal);
					if (Util.validar(item.getSubTotal())) {
						total = total + item.getSubTotal().longValue();
					}
				}
				venda.setSubTotal(BigDecimal.valueOf(total));
			}
			if (Util.validar(venda.getSubTotal()) && Util.validar(venda.getDescontoTotal())) {
				long lngSubTotal = venda.getSubTotal().longValue();
				long lngDesconto = venda.getDescontoTotal().longValue();
				if (lngDesconto >= 0 && lngDesconto <= 1) {
					lngTotal = lngSubTotal - (lngSubTotal * lngDesconto);
				}
			}
			venda.setTotal(BigDecimal.valueOf(lngTotal));
		}
		return venda;
	}
}
