package br.com.caracore.pdv.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.caracore.pdv.model.Usuario;
import br.com.caracore.pdv.service.UsuarioService;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioService usuarioService;

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		List<Usuario> listar = usuarioService.buscarTodos();
		
		for (Usuario usuario : listar) {
			
			String usr = usuario.getNome(); 
			String pwd = usuario.getSenha(); 
			String roles = usuario.getPerfil(); 
			
			if (roles.equals("ADMINISTRADOR")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("ADMINISTRADOR");
			} else if (roles.equals("USUARIO")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("USUARIO", "VISITANTE");
			} else if (roles.equals("VISITANTE")) {
				auth.inMemoryAuthentication().withUser(usr).password(pwd).roles("VISITANTE");
			}
		}
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/layout/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/esqueceu-a-senha").permitAll()
				.antMatchers("/usuarios").hasRole("VISITANTE")
				.antMatchers("/usuarios").hasRole("USUARIO")
				.antMatchers("/usuarios/**").hasRole("USUARIO")
//				.antMatchers("/usuarios/**").hasRole("ADMINISTRADOR")
				.antMatchers("/vinhos").hasRole("VISITANTE")
				.antMatchers("/vinhos/**").hasRole("USUARIO")
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
	}

}
