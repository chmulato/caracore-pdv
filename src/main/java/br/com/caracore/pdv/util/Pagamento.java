package br.com.caracore.pdv.util;

import java.math.BigDecimal;

import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Component;

import br.com.caracore.pdv.service.exception.DescontoInvalidoException;

@Component
public class Pagamento {
	
	final private double ZERO = 0d;
	
	final private double PORCENTAGEM = 100d;

	private Long codigoVenda;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal totalApagar;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal dinheiro;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal cartao;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal cheque;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal outros;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal desconto;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorDesconto;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal totalPago;

	/**
	 * Método interno para calcular o valor de desconto
	 * 
	 * @param totalAPagar
	 * @param desconto
	 * @return
	 */
	private BigDecimal valorDesconto(BigDecimal totalAPagar, BigDecimal desconto) {
		double valorDesconto = 0d;
		if (Util.validar(totalAPagar) && Util.validar(desconto)) {
			double desc = desconto.doubleValue();
			if ((desc < ZERO) || (desc > PORCENTAGEM)) {
				throw new DescontoInvalidoException("Desconto Inválido!");
			}
			valorDesconto = totalAPagar.doubleValue() * desconto.doubleValue()/PORCENTAGEM;
		}
		return new BigDecimal(valorDesconto);
	}

	public Pagamento() {
		super();
	}

	/**
	 * Construtor inicial da classe Pagamento
	 * 
	 * @param venda
	 * @param totalApagar
	 * @param desconto
	 */
	public Pagamento(Long codigoVenda, BigDecimal totalApagar, BigDecimal desconto) {
		super();
		this.codigoVenda = codigoVenda;
		this.totalApagar = totalApagar;
		this.desconto = desconto;
		this.valorDesconto = valorDesconto(totalApagar, desconto);
	}

	public Long getCodigoVenda() {
		return codigoVenda;
	}

	public void setCodigoVenda(Long codigoVenda) {
		this.codigoVenda = codigoVenda;
	}

	public BigDecimal getTotalApagar() {
		return totalApagar;
	}

	public void setTotalApagar(BigDecimal totalApagar) {
		this.totalApagar = totalApagar;
	}

	public BigDecimal getDinheiro() {
		return dinheiro;
	}

	public void setDinheiro(BigDecimal dinheiro) {
		this.dinheiro = dinheiro;
	}

	public BigDecimal getCartao() {
		return cartao;
	}

	public void setCartao(BigDecimal cartao) {
		this.cartao = cartao;
	}

	public BigDecimal getCheque() {
		return cheque;
	}

	public void setCheque(BigDecimal cheque) {
		this.cheque = cheque;
	}

	public BigDecimal getOutros() {
		return outros;
	}

	public void setOutros(BigDecimal outros) {
		this.outros = outros;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public BigDecimal getTotalPago() {
		return totalPago;
	}

	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}
	
}
