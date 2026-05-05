package com.autobots.automanager.entidades;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
public class Mercadoria extends RepresentationModel<Mercadoria> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDate dataValidade;

	@Column(nullable = false)
	private LocalDate dataFabricacao;

	@Column(nullable = false)
	private LocalDate dataCadastro;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private long quantidade;

	@Column(nullable = false)
	private double valor;

	@Column()
	private String descricao;
}