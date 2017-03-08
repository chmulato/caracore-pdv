package br.com.caracore.pdv.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Vendedor;

public class Util {

	@SuppressWarnings("rawtypes")
	public static boolean validar(Object objeto) {
		boolean result = false;
		if (objeto != null) {
			if (objeto instanceof String) {
				String str = (String) objeto;
				if (!str.equals("")) {
					result = true;
				}
			} else if (objeto instanceof List) {
				List lista = (List) objeto;
				if (lista.size() > 0) {
					result = true;
				}
			} else {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Método para formatar o valor do desconto
	 * 
	 * @param desconto
	 * @return
	 */
	public static BigDecimal formatarDesconto(String desconto) {
		BigDecimal desc = BigDecimal.ZERO;
		if (Util.validar(desconto)) {
			if (desconto.equals("0")) {
				desconto = "0.00";
			} else if (desconto.charAt(desconto.length() - 3) == ',') {
				desconto = desconto.replace(".", "");
				desconto = desconto.replace(",", ".");
			} else if ((desconto.charAt(desconto.length() - 3) == '.') && (desconto.length() <= 5)) {
			} else if ((desconto.charAt(desconto.length() - 3) == '.') && (desconto.length() > 5)) {
				desconto = desconto.replace(".", "x");
				desconto = desconto.replace(",", "");
				desconto = desconto.replace("x", ".");
			}
			desc = new BigDecimal(desconto);
		}
		return desc;
	}

	/**
	 * Método para formatar uma string
	 * 
	 * @param string
	 * @param mask
	 * @return
	 */
	public static String formatString(String string, String mask) {
		try {
			javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
			mf.setValueContainsLiteralCharacters(false);
			return mf.valueToString(string);
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return string;
	}

	public static List<Loja> criarListaDeLojas() {
		Loja loja = new Loja();
		List<Loja> lista = new ArrayList<>();
		lista.add(loja);
		return lista;
	}

	public static List<Produto> criarListaDeProdutos() {
		Produto produto = new Produto();
		List<Produto> lista = new ArrayList<>();
		lista.add(produto);
		return lista;
	}

	public static List<Vendedor> criarListaDeVendedores() {
		Vendedor vendedor = new Vendedor();
		List<Vendedor> lista = new ArrayList<>();
		lista.add(vendedor);
		return lista;
	}

	public static List<Operador> criarListaDeOperadores() {
		Operador operador = new Operador();
		List<Operador> lista = new ArrayList<>();
		lista.add(operador);
		return lista;
	}

	public static List<ItemVenda> criarListaDeItensVenda() {
		ItemVenda item = new ItemVenda();
		List<ItemVenda> lista = new ArrayList<>();
		lista.add(item);
		return lista;
	}
}
