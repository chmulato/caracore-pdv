package br.com.caracore.pdv.util;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.List;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Loja;
import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Produto;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.vo.VendaDiariaVO;

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
	 * Método para validar o numero do Cpf 
	 * 
	 * @param CPF
	 * @return
	 */
	public static boolean isCPF(String CPF) {

		// considera-se erro CPF's formados por uma sequencia de numeros iguais
		if (CPF.equals("00000000000") || CPF.equals("11111111111") || CPF.equals("22222222222")
				|| CPF.equals("33333333333") || CPF.equals("44444444444") || CPF.equals("55555555555")
				|| CPF.equals("66666666666") || CPF.equals("77777777777") || CPF.equals("88888888888")
				|| CPF.equals("99999999999") || (CPF.length() != 11))
			return (false);

		char dig10, dig11;
		int sm, i, r, num, peso;

		// "try" - protege o codigo para eventuais erros de conversao de tipo
		// (int)
		try {
			// Calculo do 1o. Digito Verificador
			sm = 0;
			peso = 10;
			for (i = 0; i < 9; i++) {
				// converte o i-esimo caractere do CPF em um numero:
				// por exemplo, transforma o caractere '0' no inteiro 0
				// (48 eh a posicao de '0' na tabela ASCII)
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig10 = '0';
			else
				dig10 = (char) (r + 48); // converte no respectivo caractere
											// numerico

			// Calculo do 2o. Digito Verificador
			sm = 0;
			peso = 11;
			for (i = 0; i < 10; i++) {
				num = (int) (CPF.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso - 1;
			}

			r = 11 - (sm % 11);
			if ((r == 10) || (r == 11))
				dig11 = '0';
			else
				dig11 = (char) (r + 48);

			// Verifica se os digitos calculados conferem com os digitos
			// informados.
			if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
				return (true);
			else
				return (false);
		} catch (InputMismatchException erro) {
			return (false);
		}
	}
	
	/**
	 * Método para formatar String em data
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static Date formataData(String data) { 
        Date date = new Date();
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            date = (java.util.Date)formatter.parse(data);
        } catch (ParseException ex) {            
            ex.printStackTrace();
        }
        return date;
	}
	
	/**
	 * Método para truncar valor para duas casas
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();
	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
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
			if ((string !=null) && (!string.equals(""))) {
				javax.swing.text.MaskFormatter mf = new javax.swing.text.MaskFormatter(mask);
				mf.setValueContainsLiteralCharacters(false);
				return mf.valueToString(string);
			} else {
				return "";
			}
		} catch (ParseException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return string;
	}

	/**
	 * Método para remover formato do cpf
	 * 
	 * @param cpf
	 * @return
	 */
	public static String removerFormatoCpf(String cpf) {
		try {
			if ((cpf != null) && (!cpf.equals(""))) {
				cpf = cpf.replace("-", "");
				cpf = cpf.replace(".", "");
			}
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return cpf;
	}

	/**
	 * Método para formatar numero
	 * 
	 * @param numero
	 * @return
	 */
	public static String formatarNumero(BigDecimal numero) {
		String formatado = "0,00";
		if (numero != null) {
			DecimalFormat df = new DecimalFormat("#,###.00");
		    formatado = df.format(numero);
		    if (validar(formatado)) {
		    	if (formatado.equals(",00")) {
		    		formatado = "0,00";
		    	}
		    }
		}
		return formatado;
	}
	
	/**
	 * Método para formatar data
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatarData(Date date, String pattern) {
		String strData = "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		strData = simpleDateFormat.format(date);
		return strData;
	}
	
	/**
	 * Método para formatar cpf
	 * 
	 * @param cpf
	 * @return
	 */
	public static String formatarCpf(String cpf) {
		String cpfFormatado = "";
		if (cpf != null && !cpf.equals("")) {
			cpfFormatado = Util.formatString(String.valueOf(cpf), "###.###.###-##");
		}
		return cpfFormatado;
	}
	
	/**
	 * Método interno auxiliar para o cálculo
	 * 
	 * @param data
	 * @return
	 */
	private static Calendar getCalendarForNow(Date data) {
	    Calendar calendar = GregorianCalendar.getInstance();
	    calendar.setTime(data);
	    return calendar;
	}
	
	/**
	 * Método interno auxiliar para o cálculo
	 * 
	 * @param calendar
	 */
	private static void setTimeToBeginningOfDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * Método interno auxiliar para o cálculo
	 * 
	 * @param calendar
	 */
	private static void setTimeToEndofDay(Calendar calendar) {
	    calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	}

	/**
	 * Método externo para recuperar data/hora inicial do mes
	 * 
	 * @param data
	 * @return
	 */
	public static Date dataHoraFinalDoMes(Date data) {
	    Calendar c = getCalendarForNow(data);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(c);
        return c.getTime();
    }
	
	/**
	 * Método externo para recuperar data/hora final do mes
	 * 
	 * @param data
	 * @return
	 */
	public static Date dataHoraInicialDoMes(Date data) {
	    Calendar c = getCalendarForNow(data);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(c);
        return c.getTime();
	}
	
	/**
	 * Método para buscar a data/hora inicial do dia
	 * 
	 * @param data
	 * @return
	 */
	public static Date dataHoraInicial(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
        setTimeToBeginningOfDay(c);
		return c.getTime();
	}

	/**
	 * Método para buscar a data/hora final do dia
	 * 
	 * @param data
	 * @return
	 */
	public static Date dataHoraFinal(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
        setTimeToEndofDay(c);
		return c.getTime();
	}
	
	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<VendaDiariaVO> criarListaVendasDiariasVO() {
		VendaDiariaVO vo = new VendaDiariaVO();
		vo.setData(new Date());
		vo.setVenda(Long.valueOf(0l));
		vo.setItens(Integer.valueOf(0));
		vo.setTotal(BigDecimal.ZERO);
		vo.setVendedor("");
		List<VendaDiariaVO> lista = new ArrayList<>();
		lista.add(vo);
		return lista;
	}
	
	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<Venda> criarListaDeVendas() {
		Venda venda = new Venda();
		List<Venda> lista = new ArrayList<>();
		lista.add(venda);
		return lista;
	}
	
	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<Loja> criarListaDeLojas() {
		Loja loja = new Loja();
		List<Loja> lista = new ArrayList<>();
		lista.add(loja);
		return lista;
	}

	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<Produto> criarListaDeProdutos() {
		Produto produto = new Produto();
		List<Produto> lista = new ArrayList<>();
		lista.add(produto);
		return lista;
	}
	
	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<Vendedor> criarListaDeVendedores() {
		Vendedor vendedor = new Vendedor();
		List<Vendedor> lista = new ArrayList<>();
		lista.add(vendedor);
		return lista;
	}

	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<Operador> criarListaDeOperadores() {
		Operador operador = new Operador();
		List<Operador> lista = new ArrayList<>();
		lista.add(operador);
		return lista;
	}

	/**
	 * Método para criar uma lista default vazia
	 * 
	 * @return
	 */
	public static List<ItemVenda> criarListaDeItensVenda() {
		ItemVenda item = new ItemVenda();
		List<ItemVenda> lista = new ArrayList<>();
		lista.add(item);
		return lista;
	}
	
}
