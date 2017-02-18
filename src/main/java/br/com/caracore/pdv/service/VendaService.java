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
import br.com.caracore.pdv.repository.filter.VendaFilter;
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
	
	private Venda recuperarVendaEmAberto(Vendedor vendedor) {
		Venda result = new Venda();
		List<Venda> lista = vendaRepository.findByVendedorAndDataAndStatus(vendedor, DATA_DE_HOJE, StatusVenda.EM_ABERTO);
		if (Util.validar(lista)) {
			if (lista.size() == QUANTIDADE_UNITARIA) {
				for (Venda venda : lista) {
					result = venda;
				}
			}
		}
		return result;
	}
	
	private void carregarItemVenda(List<ItemVenda> itens, Produto produto) {
		ItemVenda item = new ItemVenda();
		item.setDesconto(BigDecimal.ZERO);
		item.setProduto(produto);
		item.setPrecoUnitario(produto.getValor());
		item.setQuantidade(Integer.valueOf(QUANTIDADE_UNITARIA));
		itens.add(item);
	}
	
	private Usuario buscarUsuarioLogado(String login) {
		return usuarioService.pesquisarPorNome(login);
	}
	
	private Vendedor buscarVendedorDefault(Loja loja) {
		return vendedorService.buscarDefault(loja);
	}
	
	private List<Vendedor> listarVendedoresPorLoja(Loja loja) {
		return lojaService.listarVendedores(loja);
	}

	public Venda recuperarVendaEmAberto(String login) {
		Venda venda = null;
		Usuario usuario = buscarUsuarioLogado(login);
		if (Util.validar(usuario)) {
			if (Util.validar(usuario.getLoja())) {
				Loja loja = usuario.getLoja();
				Vendedor vendedorDefault = vendedorService.buscarDefault(loja);
				venda = recuperarVendaEmAberto(vendedorDefault);
			}
		}
		return venda;
	}
	
	public void salvar(Venda venda) {
		vendaRepository.save(venda);
	}

	public void excluir(Long codigo) {
		vendaRepository.delete(codigo);;
	}

	public Venda pesquisarPorId(Long codigo) {
		return vendaRepository.findOne(codigo);
	}

	public List<Venda> pesquisar(VendaFilter filtro) {
		List<Venda> lista = null;
		if (filtro.getCodigo() != null) {
			Long codigo = filtro.getCodigo();
			Venda venda = pesquisarPorId(codigo);
			lista = new ArrayList<>();
			lista.add(venda); 
		} else  if (filtro.getVendedor() != null) {
			Vendedor vendedor = filtro.getVendedor();
			lista = vendaRepository.findByVendedor(vendedor); 
		} else if (filtro.getData() != null) {
			Date data = filtro.getData();
			lista = vendaRepository.findByData(data); 
		}
		return lista; 
	}
	
	public List<Vendedor> listarVendedoresPorLogin(String login) {
		List<Vendedor> lista = null;
		Usuario usuario = buscarUsuarioLogado(login);
		if (Util.validar(usuario) && Util.validar(usuario.getLoja())) {
			Loja loja = usuario.getLoja();
			Vendedor vendedor = buscarVendedorDefault(loja);
			if (Util.validar(vendedor)) {
				lista = listarVendedoresPorLoja(loja);
			}
		}
		return lista;
	}
	
	public Venda carregarCesta(Long codigoProduto, String login) {
		Venda result = null;
		List<ItemVenda> itens = null;
		if (Util.validar(codigoProduto)) {
			Usuario usuario = buscarUsuarioLogado(login);
			if (Util.validar(usuario) && Util.validar(usuario.getLoja())) {
				Loja loja = usuario.getLoja();
				Vendedor vendedor = buscarVendedorDefault(loja);
				if (Util.validar(vendedor)) {
					Venda venda = recuperarVendaEmAberto(vendedor);
					if (Util.validar(venda)) {
						if (Util.validar(venda.getItens())) {
							itens = venda.getItens();
						} else {
							itens = new ArrayList<>();
						}
					}
					Produto produto = produtoService.pesquisarPorId(codigoProduto);
					if (Util.validar(produto)) {
						carregarItemVenda(itens, produto);
					}
					venda.setItens(itens);
					venda.setData(DATA_DE_HOJE);
					venda.setVendedor(vendedor);
					venda.setStatus(StatusVenda.EM_ABERTO);
					vendaRepository.save(venda);
					result = venda;
				}
			}
		}
		return result;
	}

}
