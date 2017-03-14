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
import net.sf.jasperreports.engine.JREmptyDataSource;
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
			        
			        parameters.put("Titulo", "Nota sem valor fiscal");
			        parameters.put("DataHora", "01/01/2017");
			        parameters.put("Dinheiro", BigDecimal.ONE);
			        parameters.put("Cheque", BigDecimal.ONE);
			        parameters.put("Outros", BigDecimal.ONE);
			        parameters.put("Cartao", BigDecimal.ONE);
			        parameters.put("Cliente", "João da Silva");
			        parameters.put("Vendedor", "Maria");
			        parameters.put("Cpf", "X49.01X.05X-XX");
			        parameters.put("Total", BigDecimal.ONE);
			        parameters.put("Troco", BigDecimal.ONE);
			        parameters.put("DescontoTotal", "10,00%");
			        parameters.put("ValorDescontoTotal", BigDecimal.ONE);
			        parameters.put("Loja", "Loja 01");
			        
			        parameters.put(DS_KEY, dados);
					
					JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

					response.setContentType("application/x-pdf");
					response.setHeader("Content-disposition", "inline; filename=nota_sem_valor_fiscal.pdf");

					final OutputStream outStream = response.getOutputStream();
					
					JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
				
				}
				
			}
			
		}
		
	}
}
