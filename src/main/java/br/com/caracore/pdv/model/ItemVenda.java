package br.com.caracore.pdv.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.NumberFormat;

@Entity
public class ItemVenda {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	@NotNull(message = "Produto é obrigatório!")
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PRODUTO_ID")
	private Produto produto;
	
	@NotNull(message = "Quantidade é obrigatória!")
	private Integer quantidade;
	
	@NotNull(message = "Preço unitário é obrigatório!")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal precoUnitario;
	
	@NumberFormat(pattern = "#0.00")
	private BigDecimal desconto;
	
	@NotNull(message = "Venda é obrigatória!")
	@OneToOne
	@JoinColumn(name = "VENDA_ID")
	private Venda venda;
	
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal subTotal;

	@Transient
	private String descricaoProduto;

	public ItemVenda() {
		super();
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(BigDecimal precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public Venda getVenda() {
		return venda;
	}

	public void setVenda(Venda venda) {
		this.venda = venda;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public String getDescricaoProduto() {
		String descricao = "";
		if ((produto != null) && ((produto.getDescricao() != null) && (!produto.getDescricao().equals("")))) {
			descricao = produto.getDescricao();
		}
		descricaoProduto = descricao;
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((desconto == null) ? 0 : desconto.hashCode());
		result = prime * result + ((precoUnitario == null) ? 0 : precoUnitario.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
		result = prime * result + ((quantidade == null) ? 0 : quantidade.hashCode());
		result = prime * result + ((subTotal == null) ? 0 : subTotal.hashCode());
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
		ItemVenda other = (ItemVenda) obj;
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
		if (precoUnitario == null) {
			if (other.precoUnitario != null)
				return false;
		} else if (!precoUnitario.equals(other.precoUnitario))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		if (subTotal == null) {
			if (other.subTotal != null)
				return false;
		} else if (!subTotal.equals(other.subTotal))
			return false;
		if (venda == null) {
			if (other.venda != null)
				return false;
		} else if (!venda.equals(other.venda))
			return false;
		return true;
	}

}
