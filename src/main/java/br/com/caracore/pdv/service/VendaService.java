package br.com.caracore.pdv.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.model.Vendedor;
import br.com.caracore.pdv.repository.VendaRepository;
import br.com.caracore.pdv.repository.filter.VendaFilter;

@Service
public class VendaService {

	@Autowired
	private VendaRepository vendaRepository;

	public void salvar(Venda venda) {
		vendaRepository.save(venda);
	}

	public void excluir(Long codigo) {
		vendaRepository.delete(codigo);;
	}

	public Venda pesquisarPorId(Long codigo) {
		return vendaRepository.findOne(codigo);
	}

	public List<Venda> pesquisar(VendaFilter filtro) {
		List<Venda> lista = null;
		if (filtro.getCodigo() != null) {
			Long codigo = filtro.getCodigo();
			Venda venda = pesquisarPorId(codigo);
			lista = new ArrayList<>();
			lista.add(venda); 
		} else  if (filtro.getVendedor() != null) {
			Vendedor vendedor = filtro.getVendedor();
			lista = vendaRepository.findByVendedor(vendedor); 
		} else if (filtro.getData() != null) {
			Date data = filtro.getData();
			lista = vendaRepository.findByData(data); 
		}
		return lista; 
	}
}
