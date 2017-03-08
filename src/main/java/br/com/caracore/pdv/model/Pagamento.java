package br.com.caracore.pdv.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

import br.com.caracore.pdv.service.exception.DescontoInvalidoException;
import br.com.caracore.pdv.util.Util;

@Entity
public class Pagamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@ManyToOne
	@JoinColumn(name = "CLIENTE_ID")
	private Cliente cliente;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "VENDA_ID")
	private Venda venda;
	
	@NotNull(message = "Total a pagar é obrigatório!")
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

	@NotNull(message = "Desconto é obrigatório!")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal desconto;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorDesconto;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal totalPago;

	@Transient
	private String cpf;
	
	/**
	 * Método interno para calcular o valor de desconto
	 * 
	 * @param totalAPagar
	 * @param desconto
	 * @return
	 */
	private BigDecimal valorDesconto(BigDecimal totalAPagar, BigDecimal desconto) {
		final double ZERO = 0d;
		final double PORCENTAGEM = 100d;
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
	 * @param descontoTotal
	 */
	public Pagamento(Cliente cliente, Venda venda, BigDecimal totalApagar, BigDecimal descontoTotal) {
		super();
		this.cliente = cliente;
		this.venda = venda;
		this.totalApagar = totalApagar;
		this.desconto = descontoTotal;
		this.valorDesconto = valorDesconto(totalApagar, descontoTotal);
	}
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartao == null) ? 0 : cartao.hashCode());
		result = prime * result + ((cheque == null) ? 0 : cheque.hashCode());
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result + ((dinheiro == null) ? 0 : dinheiro.hashCode());
		result = prime * result + ((outros == null) ? 0 : outros.hashCode());
		result = prime * result + ((totalApagar == null) ? 0 : totalApagar.hashCode());
		result = prime * result + ((totalPago == null) ? 0 : totalPago.hashCode());
		result = prime * result + ((valorDesconto == null) ? 0 : valorDesconto.hashCode());
		result = prime * result + ((venda == null) ? 0 : venda.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pagamento other = (Pagamento) obj;
		if (cartao == null) {
			if (other.cartao != null)
				return false;
		} else if (!cartao.equals(other.cartao))
			return false;
		if (cheque == null) {
			if (other.cheque != null)
				return false;
		} else if (!cheque.equals(other.cheque))
			return false;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (desconto == null) {
			if (other.desconto != null)
				return false;
		} else if (!desconto.equals(other.desconto))
			return false;
		if (dinheiro == null) {
			if (other.dinheiro != null)
				return false;
		} else if (!dinheiro.equals(other.dinheiro))
			return false;
		if (outros == null) {
			if (other.outros != null)
				return false;
		} else if (!outros.equals(other.outros))
			return false;
		if (totalApagar == null) {
			if (other.totalApagar != null)
				return false;
		} else if (!totalApagar.equals(other.totalApagar))
			return false;
		if (totalPago == null) {
			if (other.totalPago != null)
				return false;
		} else if (!totalPago.equals(other.totalPago))
			return false;
		if (valorDesconto == null) {
			if (other.valorDesconto != null)
				return false;
		} else if (!valorDesconto.equals(other.valorDesconto))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}
	
}
