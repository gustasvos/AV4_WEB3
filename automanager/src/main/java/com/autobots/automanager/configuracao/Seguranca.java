package com.autobots.automanager.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.autobots.automanager.adaptadores.UserDetailsServiceImpl;
import com.autobots.automanager.filtros.Autenticador;
import com.autobots.automanager.filtros.Autorizador;
import com.autobots.automanager.jwt.ProvedorJwt;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguranca extends WebSecurityConfigurerAdapter {

	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserDetailsServiceImpl servico;

	@Autowired
	private ProvedorJwt provedorJwt;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable();

		http.authorizeHttpRequests()
				// rota de login — pública
				.antMatchers(HttpMethod.POST, "/login").permitAll()

				// empresa — só admin
				.antMatchers("/empresas/**").hasRole("ADMIN")

				// usuarios — admin gerencia tudo, gerente gerencia abaixo dele, vendedor só cliente
				.antMatchers(HttpMethod.GET, "/usuarios/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
				.antMatchers(HttpMethod.POST, "/usuarios/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
				.antMatchers(HttpMethod.PUT, "/usuarios/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
				.antMatchers(HttpMethod.DELETE, "/usuarios/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")

				// veiculos
				.antMatchers(HttpMethod.GET, "/veiculos/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR", "CLIENTE")
				.antMatchers(HttpMethod.POST, "/veiculos/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
				.antMatchers(HttpMethod.PUT, "/veiculos/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
				.antMatchers(HttpMethod.DELETE, "/veiculos/**").hasAnyRole("ADMIN", "GERENTE")

				// mercadorias e servicos — gerente faz tudo, vendedor só lê
				.antMatchers(HttpMethod.GET, "/mercadorias/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR", "CLIENTE")
				.antMatchers(HttpMethod.POST, "/mercadorias/**").hasAnyRole("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.PUT, "/mercadorias/**").hasAnyRole("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE, "/mercadorias/**").hasAnyRole("ADMIN", "GERENTE")

				.antMatchers(HttpMethod.GET, "/servicos/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR", "CLIENTE")
				.antMatchers(HttpMethod.POST, "/servicos/**").hasAnyRole("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.PUT, "/servicos/**").hasAnyRole("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE, "/servicos/**").hasAnyRole("ADMIN", "GERENTE")

				// vendas — gerente faz tudo, vendedor cria e lê, cliente só lê
				.antMatchers(HttpMethod.GET, "/vendas/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR", "CLIENTE")
				.antMatchers(HttpMethod.POST, "/vendas/**").hasAnyRole("ADMIN", "GERENTE", "VENDEDOR")
				.antMatchers(HttpMethod.PUT, "/vendas/**").hasAnyRole("ADMIN", "GERENTE")
				.antMatchers(HttpMethod.DELETE, "/vendas/**").hasAnyRole("ADMIN", "GERENTE")

				// demais rotas
				.anyRequest().authenticated();

		http.addFilter(new Autenticador(authenticationManager(), provedorJwt));
		http.addFilter(new Autorizador(authenticationManager(), provedorJwt, servico));

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder autenticador) throws Exception {
		autenticador.userDetailsService(servico).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
		fonte.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return fonte;
	}
}