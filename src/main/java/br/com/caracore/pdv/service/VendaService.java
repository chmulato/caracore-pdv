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

	final private Date DATA_ATUAL = new Date();
	
	private Venda recuperarVendaEmAberto(Vendedor vendedor) {
		Venda result = new Venda();
		List<Venda> lista = vendaRepository.findByVendedorAndDataAndStatus(vendedor, DATA_ATUAL, StatusVenda.EM_ABERTO);
		if (lista != null && lista.size() > 0) {
			if (lista.size() == 1) {
				for (Venda venda : lista) {
					result = venda;
				}
			}
		}
		return result;
	}
	
	private Usuario buscarUsuario(String login) {
		return usuarioService.pesquisarPorNome(login);
	}
	
	private Vendedor buscarVendedorDefault() {
		return vendedorService.buscarDefault();
	}
	
	private List<Vendedor> listarVendedoresPorLoja(Loja loja) {
		return lojaService.listarVendedores(loja);
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
		Usuario usuario = buscarUsuario(login);
		if (Util.validar(usuario)) {
			Vendedor vendedor = buscarVendedorDefault();
			if (Util.validar(vendedor)) {
				Loja loja = vendedor.getLoja();
				lista = listarVendedoresPorLoja(loja);
			}
		}
		return lista;
	}
	
	public Venda cadastrar(Long codigoProduto, String login) {
		Venda result = null;
		if (Util.validar(codigoProduto)) {
			Usuario usuario = buscarUsuario(login);
			if (Util.validar(usuario)) {
				Vendedor vendedor = buscarVendedorDefault();
				if (Util.validar(vendedor)) {
					Venda venda = recuperarVendaEmAberto(vendedor);
					List<ItemVenda> itens = new ArrayList<>();
					if (Util.validar(venda)) {
						if (venda.getItens() != null && venda.getItens().size() > 0) {
							itens = venda.getItens();
						}
					} else {
						venda.setData(DATA_ATUAL);
						venda.setVendedor(vendedor);
						venda.setStatus(StatusVenda.EM_ABERTO);
					}
					Produto produto = produtoService.pesquisarPorId(codigoProduto);
					if (Util.validar(produto)) {
						ItemVenda item = new ItemVenda();
						item.setDesconto(BigDecimal.ZERO);
						item.setProduto(produto);
						item.setQuantidade(Integer.valueOf(1));
						item.setPrecoUnitario(produto.getValor());
						itens.add(item);
					}
					venda.setItens(itens);
					result = venda;
				}
			}
		}
		return result;
	}

}
