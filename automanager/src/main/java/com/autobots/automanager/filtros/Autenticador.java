package com.autobots.automanager.filtros;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.autobots.automanager.dto.LoginDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Autenticador extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager gerenciadorAutenticacao;
	private ProvedorJwt provedorJwt;

	public Autenticador(AuthenticationManager gerenciadorAutenticacao, ProvedorJwt provedorJwt) {
		this.gerenciadorAutenticacao = gerenciadorAutenticacao;
		this.provedorJwt = provedorJwt;
		setFilterProcessesUrl("/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		LoginDTO login;
		try {
			login = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
		} catch (IOException e) {
			login = new LoginDTO("", "");
		}
		UsernamePasswordAuthenticationToken dadosAutenticacao = new UsernamePasswordAuthenticationToken(
				login.nomeUsuario(), login.senha(), new ArrayList<>());
		return gerenciadorAutenticacao.authenticate(dadosAutenticacao);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
											FilterChain chain, Authentication autenticacao) throws IOException, ServletException {
		UserDetails usuario = (UserDetails) autenticacao.getPrincipal();
		String jwt = provedorJwt.proverJwt(usuario.getUsername());
		response.addHeader("Authorization", "Bearer " + jwt);
	}
}