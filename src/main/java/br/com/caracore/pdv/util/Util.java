package br.com.caracore.pdv.util;

import java.util.List;

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

}
