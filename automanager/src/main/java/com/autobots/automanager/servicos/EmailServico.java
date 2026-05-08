package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkEmail;
import com.autobots.automanager.modelos.atualizador.EmailAtualizador;
import com.autobots.automanager.repositorios.EmailRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class EmailServico {

    @Autowired
    private EmailRepositorio repositorio;
    @Autowired
    private AdicionadorLinkEmail adicionadorLinkEmail;
    @Autowired
    private EmailAtualizador atualizador;

    public ResponseEntity<Email> obterEmail(long id) {
        return repositorio.findById(id)
                .map(email -> {
                    adicionadorLinkEmail.adicionarLink(email);
                    return ResponseEntity.ok(email);
                })
                .orElse(ResponseEntity.notFound().build());
//
    }

    public ResponseEntity<List<Email>> obterEmails() {
        List<Email> emails = repositorio.findAll();
        adicionadorLinkEmail.adicionarLink(emails);
        return ResponseEntity.ok(emails);
//        
    }

    public ResponseEntity<Email> cadastrarEmail(EmailDTO dto) {
        Email email = new Email();
        email.setEndereco(dto.endereco());

        Email salvo = repositorio.save(email);
        adicionadorLinkEmail.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmailControle.class)
                        .obterEmail(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Email> atualizarEmail(long id, EmailDTO dto) {
        return repositorio.findById(id)
                .map(email -> {
                    atualizador.atualizar(email, dto);
                    Email salvo = repositorio.save(email);
                    adicionadorLinkEmail.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}