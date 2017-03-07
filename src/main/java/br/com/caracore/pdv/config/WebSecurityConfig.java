package br.com.caracore.pdv.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.caracore.pdv.model.Operador;
import br.com.caracore.pdv.service.OperadorService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private OperadorService operadorService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/","/esqueceu-a-senha", "/recupera-senha").permitAll()
				.antMatchers("/clientes").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/clientes/**").hasAnyRole("ADMINISTRADOR","OPERADOR")
				.antMatchers("/itens").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/itens/**").hasAnyRole("ADMINISTRADOR","OPERADOR")
				.antMatchers("/lojas").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/lojas/**").hasAnyRole("ADMINISTRADOR","OPERADOR")
				.antMatchers("/operadores").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/operadores/**").hasRole("ADMINISTRADOR")
				.antMatchers("/pagamentos").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/pagamentos/**").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/produtos").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/produtos/**").hasAnyRole("ADMINISTRADOR","OPERADOR")
				.antMatchers("/vendas").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/vendas/**").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/vendedores").hasAnyRole("ADMINISTRADOR","OPERADOR","VISITANTE")
				.antMatchers("/vendedores/**").hasAnyRole("ADMINISTRADOR","OPERADOR")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

	@Autowired
	protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
		List<Operador> listar = operadorService.buscarTodos();
		
		for (Operador OPERADOR : listar) {
			
			String usr = OPERADOR.getNome(); 
			String pwd = OPERADOR.getSenha(); 
			String roles = OPERADOR.getPerfil().toString(); 
			
			if (roles.equals("ADMINISTRADOR")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("ADMINISTRADOR","OPERADOR","VISITANTE");
			} else if (roles.equals("OPERADOR")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("OPERADOR", "VISITANTE");
			} else if (roles.equals("VISITANTE")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("VISITANTE");
			}
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		//web.debug(true);
		web.ignoring().antMatchers("/layout/**");
	}

}
