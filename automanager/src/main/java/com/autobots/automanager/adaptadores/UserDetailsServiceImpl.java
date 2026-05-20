package com.autobots.automanager.adaptadores;

import java.util.List;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepositorio repositorio;

	private Usuario obterPorNome(String nomeUsuario) {
		return repositorio.findAll().stream()
				.filter(usuario -> usuario.getCredenciais().stream()
						.filter(c -> c instanceof CredencialUsuarioSenha)
						.map(c -> ((CredencialUsuarioSenha) c).getNomeUsuario())
						.anyMatch(nome -> nome.equals(nomeUsuario)))
				.findFirst()
				.orElse(null);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario selecionado = obterPorNome(username);
		if (selecionado == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(selecionado);
	}
}