package br.com.caracore.pdv.util;

import java.util.ArrayList;
import java.util.List;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Usuario;
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

	public static List<Loja> criarListaDeLojas(){
		Loja loja = new Loja();
		List<Loja> lista = new ArrayList<>();
		lista.add(loja);
		return lista;
	}

	public static List<Vendedor> criarListaDeVendedores(){
		Vendedor vendedor = new Vendedor();
		List<Vendedor> lista = new ArrayList<>();
		lista.add(vendedor);
		return lista;
	}
	
	public static List<Usuario> criarListaDeUsuarios(){
		Usuario usuario = new Usuario();
		List<Usuario> lista = new ArrayList<>();
		lista.add(usuario);
		return lista;
	}
	
	public static List<ItemVenda> criarListaDeItensVenda(){
		ItemVenda item = new ItemVenda();
		List<ItemVenda> lista = new ArrayList<>();
		lista.add(item);
		return lista;
	}
}
