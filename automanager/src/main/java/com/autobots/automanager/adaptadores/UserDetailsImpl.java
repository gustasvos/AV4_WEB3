package com.autobots.automanager.adaptadores;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.enumeracoes.PerfilAcesso;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.autobots.automanager.entidades.Usuario;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails {
	private Usuario usuario;

	public UserDetailsImpl(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> autoridades = new ArrayList<>();
		for (PerfilAcesso perfil : usuario.getPerfisAcesso()) {
			autoridades.add(new SimpleGrantedAuthority(perfil.name()));
		}
		return autoridades;
	}

	@Override
	public String getPassword() {
		return usuario.getCredenciais().stream()
				.filter(c -> c instanceof CredencialUsuarioSenha)
				.map(c -> ((CredencialUsuarioSenha) c).getSenha())
				.findFirst()
				.orElse(null);
	}

	@Override
	public String getUsername() {
		return usuario.getCredenciais().stream()
				.filter(c -> c instanceof CredencialUsuarioSenha)
				.map(c -> ((CredencialUsuarioSenha) c).getNomeUsuario())
				.findFirst()
				.orElse(null);
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return usuario.getCredenciais().stream()
				.filter(c -> c instanceof CredencialUsuarioSenha)
				.map(c -> !c.isInativo())
				.findFirst()
				.orElse(false);
	}
}