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

	@Autowired
	private ItemVendaService itemVendaService;
	
	public Venda recuperarVendaEmAberto(Vendedor vendedor) {
		Venda result = new Venda();
		result.setVendedor(vendedor);
		List<Venda> lista = vendaRepository.findByVendedorAndDataAndStatus(vendedor, DATA_DE_HOJE, StatusVenda.EM_ABERTO);
		if (Util.validar(lista)) {
			if (lista.size() == QUANTIDADE_UNITARIA) {
				for (Venda venda : lista) {
					if (Util.validar(venda.getItens())) {
						List<ItemVenda> itens = itemVendaService.buscarItens(venda);
						if (Util.validar(itens)) {
							venda.setItens(itens);
						}
					}
					result = venda;
				}
			}
		}
		return result;
	}
	
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
	
	private List<Vendedor> listarVendedoresPorLoja(Loja loja) {
		return lojaService.listarVendedores(loja);
	}

	public Usuario recuperarUsuario(String login) {
		Usuario usuario = null;
		if (Util.validar(login)) {
			usuario = usuarioService.pesquisarPorNome(login); 
		}
		return usuario;
	}
	
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

	public Venda salvar(Venda venda) {
		if (Util.validar(venda)) {
			venda.setData(DATA_DE_HOJE);
		}
		venda = vendaRepository.save(venda);
		return venda;
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
	
	public Venda carregarCesta(Long codigoProduto, Usuario usuario) {
		Venda result = null;
		boolean step = false;
		ItemVenda novoItem = new ItemVenda();
		if (Util.validar(codigoProduto)) {
			Produto produto = produtoService.pesquisarPorId(codigoProduto);
			if (Util.validar(produto)) {
				novoItem.setDesconto(BigDecimal.ZERO);
				novoItem.setProduto(produto);
				novoItem.setPrecoUnitario(produto.getValor());
				novoItem.setQuantidade(Integer.valueOf(QUANTIDADE_UNITARIA));
				step = true;
			}
		}
		if (step) {
			if (Util.validar(usuario)) {
				Vendedor vendedor = buscarVendedor(usuario);
				if (Util.validar(vendedor)) {
					Venda venda = recuperarVendaEmAberto(vendedor);
					if (Util.validar(venda)) {
						
						// setar valores defaults
						venda.setVendedor(vendedor);
						venda.setDescontoTotal(BigDecimal.ZERO);
						venda.setStatus(StatusVenda.EM_ABERTO);
						
						if (Util.validar(venda.getCodigo())) {
							List<ItemVenda> itens = itemVendaService.buscarItens(venda);
							if (Util.validar(itens)) {
								itens.add(novoItem);
								venda.setItens(itens);
							}
							venda = salvar(venda);
							itemVendaService.salvarItens(itens, venda);
						} else {
							List<ItemVenda> itens = new ArrayList<>();
							itens.add(novoItem);
							venda.setItens(itens);
							venda = salvar(venda);
						}
					}
					result = venda;
				}
			}
		}
		return result;
	}


}
