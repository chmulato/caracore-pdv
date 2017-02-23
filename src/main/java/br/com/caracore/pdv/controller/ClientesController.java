package br.com.caracore.pdv.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
	public ModelAndView salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(cliente);
		}
		clienteService.salvar(cliente);
		attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
		return new ModelAndView("redirect:/clientes/novo");
	}
	
	@GetMapping
	public ModelAndView pesquisar(ClienteFilter filtroCliente) {
		ModelAndView mv = new ModelAndView("cliente/pesquisa-clientes");
		if (filtroCliente != null) {
			mv.addObject("clientes", clienteService.pesquisar(filtroCliente));
		} else {
			filtroCliente = new ClienteFilter();
			filtroCliente.setNome("%");
		}
		return mv;		
	}
	
	@GetMapping("{codigo}")
	public ModelAndView editar(@PathVariable Long codigo) {
		Cliente cliente = clienteService.pesquisarPorCodigo(codigo);
		return novo(cliente);
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.DELETE)
	public String apagar(@PathVariable("codigo") Long codigo, RedirectAttributes attributes) {
		clienteService.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Cliente removido com sucesso!");
		return "redirect:/clientes";
	}

}
