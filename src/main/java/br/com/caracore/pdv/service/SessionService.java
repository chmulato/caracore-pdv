package br.com.caracore.pdv.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import br.com.caracore.pdv.util.Util;
import br.com.caracore.pdv.vo.SessionVO;

@SessionScope
@Service
public class SessionService {
	
	private SessionVO sessionVO;

	public SessionVO getSessionVO() {
		SessionVO sessionVO = null;
		if ((Util.validar(this.sessionVO)) && (Util.validar(this.sessionVO.getAutenticado()))) {
			boolean auth = this.sessionVO.getAutenticado().booleanValue();
			if (auth) {
				sessionVO = this.sessionVO;
			}
		}
		return sessionVO;
	}

	public void setSessionVO(SessionVO sessionVO) {
		this.sessionVO = sessionVO;
	}
	
}
