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

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.service.UsuarioService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioService usuarioService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/","/esqueceu-a-senha", "/recupera-senha").permitAll()
				.antMatchers("/clientes").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/clientes/**").hasAnyRole("ADMINISTRADOR", "USUARIO")
				.antMatchers("/itens").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/itens/**").hasAnyRole("ADMINISTRADOR", "USUARIO")
				.antMatchers("/lojas").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/lojas/**").hasAnyRole("ADMINISTRADOR", "USUARIO")
				.antMatchers("/produtos").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/produtos/**").hasAnyRole("ADMINISTRADOR", "USUARIO")
				.antMatchers("/usuarios").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/usuarios/**").hasRole("ADMINISTRADOR")
				.antMatchers("/vendedores").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/vendedores/**").hasAnyRole("ADMINISTRADOR", "USUARIO")
				.antMatchers("/vendas").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
				.antMatchers("/vendas/**").hasAnyRole("ADMINISTRADOR", "USUARIO", "VISITANTE")
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
		
		List<Usuario> listar = usuarioService.buscarTodos();
		
		for (Usuario usuario : listar) {
			
			String usr = usuario.getNome(); 
			String pwd = usuario.getSenha(); 
			String roles = usuario.getPerfil().toString(); 
			
			if (roles.equals("ADMINISTRADOR")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("ADMINISTRADOR", "USUARIO", "VISITANTE");
			} else if (roles.equals("USUARIO")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("USUARIO", "VISITANTE");
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
