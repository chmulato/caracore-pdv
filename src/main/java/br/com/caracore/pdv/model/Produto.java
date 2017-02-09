package br.com.caracore.pdv.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import br.com.caracore.pdv.model.types.ControlarEstoque;
import br.com.caracore.pdv.model.types.TipoProduto;
import br.com.caracore.pdv.model.types.Unidade;

@Entity
public class Produto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	
	private Long codigoBarra;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Unidade unidade;
	
	@NotEmpty(message = "Nome do produto/serviço é obrigatório!")
	@Size(max = 30, message = "A descrição não pode conter mais de 30 caracteres.")
	private String nome;
	
	@Size(max = 60, message = "A descrição não pode conter mais de 60 caracteres.")
	private String descricao;
	
	private String referencia;
	
	private String localizacao;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataValidade;

	@NotNull(message = "Data de atualização é obrigatória!")
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Temporal(TemporalType.DATE)
	private Date dataAtualizacao;
	
	@OneToOne
	@JoinColumn(name = "MARCA_ID")
	private Marca marca;
	
	@OneToOne
	@JoinColumn(name = "CATEGORIA_ID")
	private Categoria categoria;
	
	private String imagem;
	
	private Integer estoqueMinimo;
	
	private Integer estoqueAtual;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private ControlarEstoque controlarEstoque;
	
	@NotNull(message = "Valor de custo é obrigatório!")
	@DecimalMin(value = "0.01", message = "Valor não pode ser menor que 0,01")
	@DecimalMax(value = "9999999.99", message = "Valor não pode ser maior que 9.999.999,99")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal valorCusto;

	@NotNull(message = "A margem para o valor a vista é obrigatório!")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal margemAVista;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal margemAPrazo;

	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal margemAtacado;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Long getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(Long codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}

	public Date getDataValidade() {
		return dataValidade;
	}

	public void setDataValidade(Date dataValidade) {
		this.dataValidade = dataValidade;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public Integer getEstoqueMinimo() {
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Integer estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public Integer getEstoqueAtual() {
		return estoqueAtual;
	}

	public void setEstoqueAtual(Integer estoqueAtual) {
		this.estoqueAtual = estoqueAtual;
	}

	public ControlarEstoque getControlarEstoque() {
		return controlarEstoque;
	}

	public void setControlarEstoque(ControlarEstoque controlarEstoque) {
		this.controlarEstoque = controlarEstoque;
	}

	public BigDecimal getValorCusto() {
		return valorCusto;
	}

	public void setValorCusto(BigDecimal valorCusto) {
		this.valorCusto = valorCusto;
	}

	public BigDecimal getMargemAVista() {
		return margemAVista;
	}

	public void setMargemAVista(BigDecimal margemAVista) {
		this.margemAVista = margemAVista;
	}

	public BigDecimal getMargemAPrazo() {
		return margemAPrazo;
	}

	public void setMargemAPrazo(BigDecimal margemAPrazo) {
		this.margemAPrazo = margemAPrazo;
	}

	public BigDecimal getMargemAtacado() {
		return margemAtacado;
	}

	public void setMargemAtacado(BigDecimal margemAtacado) {
		this.margemAtacado = margemAtacado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((codigoBarra == null) ? 0 : codigoBarra.hashCode());
		result = prime * result + ((controlarEstoque == null) ? 0 : controlarEstoque.hashCode());
		result = prime * result + ((dataAtualizacao == null) ? 0 : dataAtualizacao.hashCode());
		result = prime * result + ((dataValidade == null) ? 0 : dataValidade.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
		result = prime * result + ((estoqueAtual == null) ? 0 : estoqueAtual.hashCode());
		result = prime * result + ((estoqueMinimo == null) ? 0 : estoqueMinimo.hashCode());
		result = prime * result + ((imagem == null) ? 0 : imagem.hashCode());
		result = prime * result + ((localizacao == null) ? 0 : localizacao.hashCode());
		result = prime * result + ((marca == null) ? 0 : marca.hashCode());
		result = prime * result + ((margemAPrazo == null) ? 0 : margemAPrazo.hashCode());
		result = prime * result + ((margemAVista == null) ? 0 : margemAVista.hashCode());
		result = prime * result + ((margemAtacado == null) ? 0 : margemAtacado.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((referencia == null) ? 0 : referencia.hashCode());
		result = prime * result + ((tipoProduto == null) ? 0 : tipoProduto.hashCode());
		result = prime * result + ((unidade == null) ? 0 : unidade.hashCode());
		result = prime * result + ((valorCusto == null) ? 0 : valorCusto.hashCode());
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
		Produto other = (Produto) obj;
		if (categoria == null) {
			if (other.categoria != null)
				return false;
		} else if (!categoria.equals(other.categoria))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (codigoBarra == null) {
			if (other.codigoBarra != null)
				return false;
		} else if (!codigoBarra.equals(other.codigoBarra))
			return false;
		if (controlarEstoque != other.controlarEstoque)
			return false;
		if (dataAtualizacao == null) {
			if (other.dataAtualizacao != null)
				return false;
		} else if (!dataAtualizacao.equals(other.dataAtualizacao))
			return false;
		if (dataValidade == null) {
			if (other.dataValidade != null)
				return false;
		} else if (!dataValidade.equals(other.dataValidade))
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (estoqueAtual == null) {
			if (other.estoqueAtual != null)
				return false;
		} else if (!estoqueAtual.equals(other.estoqueAtual))
			return false;
		if (estoqueMinimo == null) {
			if (other.estoqueMinimo != null)
				return false;
		} else if (!estoqueMinimo.equals(other.estoqueMinimo))
			return false;
		if (imagem == null) {
			if (other.imagem != null)
				return false;
		} else if (!imagem.equals(other.imagem))
			return false;
		if (localizacao == null) {
			if (other.localizacao != null)
				return false;
		} else if (!localizacao.equals(other.localizacao))
			return false;
		if (marca == null) {
			if (other.marca != null)
				return false;
		} else if (!marca.equals(other.marca))
			return false;
		if (margemAPrazo == null) {
			if (other.margemAPrazo != null)
				return false;
		} else if (!margemAPrazo.equals(other.margemAPrazo))
			return false;
		if (margemAVista == null) {
			if (other.margemAVista != null)
				return false;
		} else if (!margemAVista.equals(other.margemAVista))
			return false;
		if (margemAtacado == null) {
			if (other.margemAtacado != null)
				return false;
		} else if (!margemAtacado.equals(other.margemAtacado))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (referencia == null) {
			if (other.referencia != null)
				return false;
		} else if (!referencia.equals(other.referencia))
			return false;
		if (tipoProduto != other.tipoProduto)
			return false;
		if (unidade != other.unidade)
			return false;
		if (valorCusto == null) {
			if (other.valorCusto != null)
				return false;
		} else if (!valorCusto.equals(other.valorCusto))
			return false;
		return true;
	}

}
