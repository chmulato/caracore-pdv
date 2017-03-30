package br.com.caracore.pdv.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.util.Util;
import br.com.caracore.pdv.vo.CompraVO;

@Service @SessionScope
public class SessionService {
	
	private CompraVO sessionVO;

	/**
	 * Método interno para alterar os dados da sessão
	 * 
	 * @param sessionVO
	 */
	private void setSessionVO(CompraVO sessionVO) {
		this.sessionVO = sessionVO;
	}

	/**
	 * Método externo para recuperar os dados da sessão
	 * 
	 * @return
	 */
	public CompraVO getSessionVO() {
		return sessionVO;
	}

	/**
	 * Método para salvar o operador da sessão
	 * 
	 * @param operador
	 */
	public void setSession(Operador operador) {
		if ((Util.validar(operador)) && (Util.validar(operador.getCodigo()))) {
			CompraVO vo = null;
			if (!Util.validar(getSessionVO())) {
				vo = new CompraVO();
			} else {
				vo = getSessionVO();
			}
			vo.setOperador(operador);
			setSessionVO(vo);
		}
	}

	/**
	 * Método para salvar o vendedor após salvar o operador
	 * 
	 * @param operador
	 * @param vendedor
	 */
	public void setSession(Operador operador, Vendedor vendedor) {
		if (Util.validar(operador)) {
			if (Util.validar(getSessionVO())) {
				CompraVO vo = getSessionVO();
				if (Util.validar(vo.getOperador())) {
					Long voId = vo.getOperador().getCodigo();
					if (voId.equals(operador.getCodigo())) {
						if (Util.validar(vendedor)) {
							vo.setVendedor(vendedor);
						}
					}
				}
			}
		}
	}

	/**
	 * Método para salvar o vendedor e a venda após salvar o operador
	 * 
	 * @param operador
	 * @param venda
	 * @param vendedor
	 */
	public void setSession(Operador operador, Venda venda, Vendedor vendedor) {
		if (Util.validar(operador)) {
			if (Util.validar(getSessionVO())) {
				CompraVO vo = getSessionVO();
				if (Util.validar(vo.getOperador())) {
					Long voId = vo.getOperador().getCodigo();
					if (voId.equals(operador.getCodigo())) {
						if (Util.validar(venda)) {
							vo.setVenda(venda);
						}
						if (Util.validar(vendedor)) {
							vo.setVendedor(vendedor);
						}
					}
				}
			}
		}
	}
	
}
