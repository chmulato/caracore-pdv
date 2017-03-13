package br.com.caracore.pdv.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.util.CompraVO;
import br.com.caracore.pdv.util.ItemVO;
import br.com.caracore.pdv.util.Util;

@Service
public class RelatorioService {

	@Autowired
	private ItemVendaService itemVendaService;
	
	@Autowired
	private PagamentoService pagamentoService;
	
	/**
	 * Método externo para buscar lista de compras por código do pagamento.
	 * 
	 * @param codigoPagamento
	 * @return
	 */
	public List<CompraVO> buscarVendaPorCodigoPagamento(Long codigoPagamento) {
		List<CompraVO> compras = null;
		CompraVO vo = null; 
		if (Util.validar(codigoPagamento)) {
			Pagamento pagamento = pagamentoService.pesquisarPorCodigo(codigoPagamento);
			if (Util.validar(pagamento)) {
				compras = new ArrayList<>();
				if (Util.validar(pagamento.getVenda())) {
					if (Util.validar(pagamento.getVenda().getCodigo())) {
						vo = new CompraVO();
						Venda venda = pagamento.getVenda();
						List<ItemVenda> itens = itemVendaService.buscarItens(venda);
						if (Util.validar(itens)) {
							vo.setItens(transferirLista(itens));
						}
						if (Util.validar(venda.getCliente())) {
							Cliente cliente = venda.getCliente();
							vo.setCliente(cliente.getNome());
							vo.setCpf(cliente.getCpfFormatado());
						}
						if (Util.validar(venda.getDescontoTotal())) {
							BigDecimal desconto = venda.getDescontoTotal();
							vo.setDesconto(Util.formatarNumero(desconto));
						}
						if (Util.validar(venda.getVendedor())) {
							Vendedor vendedor = venda.getVendedor();
							vo.setVendedor(vendedor.getNome());
							if (Util.validar(vendedor.getLoja())) {
								Loja loja = vendedor.getLoja();
								vo.setLoja(loja.getNome());
							}
						}
						compras.add(vo);
					}
				}
			}
		}
		return compras;
	}

	/**
	 * Método interno para transferir lista de itens da compra para o relatorio
	 * 
	 * @param itens
	 * @return
	 */
	private List<ItemVO> transferirLista(List<ItemVenda> itens) {
		List<ItemVO> lista = null;
		if (Util.validar(itens)) {
			lista = new ArrayList<>();
			for (ItemVenda item : itens) {
				ItemVO itemVO = new ItemVO();
				if (Util.validar(item.getCodigo())) {
					itemVO.setCodigo(String.valueOf(item.getCodigo()));
				}
				if (Util.validar(item.getDesconto())) {
					itemVO.setDesconto(Util.formatarNumero(item.getDesconto()));
				}
				if (Util.validar(item.getPrecoUnitario())) {
					itemVO.setPrecoUnitario(Util.formatarNumero(item.getPrecoUnitario()));
				}
				if (Util.validar(item.getProduto())) {
					itemVO.setProduto(item.getProduto().getDescricao());
				}
				if (Util.validar(item.getQuantidade())) {
					itemVO.setQuantidade(String.valueOf(item.getQuantidade()));
				}
				if (Util.validar(item.getSubTotal())) {
					itemVO.setSubTotal(Util.formatarNumero(item.getSubTotal()));
				}
				lista.add(itemVO);
			}
		}
		return lista;
	}

}
