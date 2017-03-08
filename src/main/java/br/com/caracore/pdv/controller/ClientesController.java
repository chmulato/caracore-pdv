package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.caracore.pdv.model.Cliente;
import br.com.caracore.pdv.repository.filter.ClienteFilter;
import br.com.caracore.pdv.service.ClienteService;
import br.com.caracore.pdv.service.exception.CpfExistenteException;
import br.com.caracore.pdv.service.exception.CpfInvalidoException;
import br.com.caracore.pdv.service.exception.NomeExistenteException;
import br.com.caracore.pdv.service.exception.ViolacaoIntegridadeException;

@Controller
@RequestMapping("/clientes")
public class ClientesController {
	
	@Autowired
	private ClienteService clienteService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Cliente cliente) {
		ModelAndView mv = new ModelAndView("cliente/cadastro-cliente");
		mv.addObject(cliente);
		return mv;
	}
	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Cliente cliente, Errors errors, RedirectAttributes attributes) {
		if (errors.hasErrors()) {
			return novo(cliente);
		}
		try {
			clienteService.salvar(cliente);
			attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
			return new ModelAndView("redirect:/clientes/novo");
		} catch (CpfInvalidoException ex) {
			errors.rejectValue("cpf", " ", ex.getMessage());
			return novo(cliente);
		} catch (CpfExistenteException ex) {
			errors.rejectValue("cpf", " ", ex.getMessage());
			return novo(cliente);
		} catch (NomeExistenteException ex) {
			errors.rejectValue("nome", " ", ex.getMessage());
			return novo(cliente);
		}
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter clienteFilter) {
		ModelAndView mv = new ModelAndView("cliente/pesquisa-clientes");
		if (clienteFilter != null) {
			mv.addObject("clientes", clienteService.pesquisar(clienteFilter));
		} else {
			clienteFilter = new ClienteFilter();
			clienteFilter.setNome("%");
		}
		mv.addObject(clienteFilter);
		return mv;		
	}
	
	@GetMapping("{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Cliente cliente = clienteService.pesquisarPorCodigo(codigo);
		return novo(cliente);
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		try {
			clienteService.excluir(codigo);
			attributes.addFlashAttribute("mensagem", "Cliente removido com sucesso!");
		} catch (ViolacaoIntegridadeException ex) {
			attributes.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/clientes";
	}

}
