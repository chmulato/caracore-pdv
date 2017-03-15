package br.com.caracore.pdv.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.caracore.pdv.model.ItemVenda;
import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.model.Venda;
import br.com.caracore.pdv.service.RelatorioService;
import br.com.caracore.pdv.util.Util;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping("/relatorios")
public class RelatoriosController {

	final private static String DS_KEY = "dados";

	final private static BigDecimal ZERO_REAL = BigDecimal.ZERO;
	
	@Autowired
	RelatorioService relatorioService;
	
	@GetMapping("/compras/{codigoPagamento}")
	@ResponseBody
	public void getReportCompras(HttpServletResponse response, @PathVariable Long codigoPagamento) throws JRException, IOException {

		Pagamento pagamento = relatorioService.buscarPorCodigoPagamento(codigoPagamento);
		
		if (Util.validar(pagamento)) {

			if (Util.validar(pagamento.getVenda())) {
				
				Venda venda = pagamento.getVenda();
				List<ItemVenda> itens = relatorioService.buscarPorVenda(venda);
				
				if (Util.validar(itens)) {
					
					InputStream jasperStream = this.getClass().getResourceAsStream("/reports/relatorio_compras.jasper");

					JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(itens);
					
					Map<String, Object> parameters = new HashMap<>();
			        
			        parameters.put("Titulo", "NOTA SEM VALOR FISCAL");
			        parameters.put("DataHora", Util.formatarData(venda.getData(), "dd/MM/yyyy hh:mm:ss"));
			        
			        if (Util.validar(pagamento.getDinheiro())) {
				        parameters.put("Dinheiro", pagamento.getDinheiro());
			        } else {
				        parameters.put("Dinheiro", ZERO_REAL);
			        }
			        
			        if (Util.validar(pagamento.getCheque())) {
				        parameters.put("Cheque", pagamento.getCheque());
			        } else {
				        parameters.put("Cheque", ZERO_REAL);
			        }

			        if (Util.validar(pagamento.getOutros())) {
				        parameters.put("Outros", pagamento.getOutros());
			        } else {
				        parameters.put("Outros", ZERO_REAL);
			        }

			        if (Util.validar(pagamento.getCartao())) {
				        parameters.put("Cartao", pagamento.getCartao());
			        } else {
				        parameters.put("Cartao", ZERO_REAL);
			        }
			        
			        if (Util.validar(pagamento.getCpf())) {
			        	String cpf = pagamento.getCpf();
				        parameters.put("Cliente", relatorioService.cliente(venda, cpf));
				        parameters.put("Cpf", Util.formatarCpf(cpf));
			        } else {
				        parameters.put("Cliente", "");
				        parameters.put("Cpf", "");
			        }
			        
			        parameters.put("Vendedor", relatorioService.vendedor(venda));
			        
			        if (Util.validar(pagamento.getTotalApagar())) {
				        parameters.put("Total", pagamento.getTotalApagar());
			        } else {
				        parameters.put("Total", ZERO_REAL);
			        }
			        
			        if (Util.validar(pagamento.getTroco())) {
				        parameters.put("Troco", pagamento.getTroco());
			        } else {
				        parameters.put("Troco", ZERO_REAL);
			        }
			        
			        if (Util.validar(pagamento.getDesconto())) {
				        parameters.put("DescontoTotal", pagamento.getDesconto());
			        } else {
				        parameters.put("DescontoTotal", ZERO_REAL);
			        }
			        
			        if (Util.validar(pagamento.getValorDesconto())) {
				        parameters.put("ValorDescontoTotal", pagamento.getValorDesconto());
			        } else {
				        parameters.put("ValorDescontoTotal", ZERO_REAL);
			        }
			        
			        parameters.put("Loja", relatorioService.loja(venda));
			        
			        parameters.put(DS_KEY, dados);
					
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dados);

					response.setContentType("application/x-pdf");
					response.setHeader("Content-disposition", "inline; filename=nota_sem_valor_fiscal.pdf");

					final OutputStream outStream = response.getOutputStream();
					
					JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
				
				}
				
			}
			
		}
		
	}
}
