package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class CredencialUsuarioSenha extends Credencial {

	@Column(nullable = false, unique = true)
	private String nomeUsuario;

	@JsonIgnore
	@Column(nullable = false)
	private String senha;
}