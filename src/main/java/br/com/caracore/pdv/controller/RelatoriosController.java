package br.com.caracore.pdv.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.caracore.pdv.model.Pagamento;
import br.com.caracore.pdv.service.PagamentoService;
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

	private List<Pagamento> pagamentos;
	
	@Autowired
	ServletContext context; 
	
	@Autowired
	PagamentoService pagamentoService;
	
	@GetMapping("/compras/{codigo}")
	@ResponseBody
	public void getReportCompras(HttpServletResponse response, @PathVariable Long codigo) throws JRException, IOException {

		Pagamento pagamento = pagamentoService.pesquisarPorCodigo(codigo);
		
		if (pagamento != null) {

			pagamentos = new ArrayList<>();
			pagamentos.add(pagamento);

			InputStream jasperStream = this.getClass().getResourceAsStream("/reports/relatorio_compras.jasper");

			JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(pagamentos);
			
			Map<String, Object> parameters = new HashMap<>();
	        
	        parameters.put("TITULO", "Lista de Compras");
	        parameters.put("EMPRESA", "Cara Core Informática");
	        parameters.put("DATAINICIO", "01/01/2017");
	        parameters.put("DATAFIM", "31/03/2017");
	        
	        parameters.put(DS_KEY, dados);
			
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

			response.setContentType("application/x-pdf");
			response.setHeader("Content-disposition", "inline; filename=lista_de_compras.pdf");

			final OutputStream outStream = response.getOutputStream();
			
			JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
		
		}
	}
}
