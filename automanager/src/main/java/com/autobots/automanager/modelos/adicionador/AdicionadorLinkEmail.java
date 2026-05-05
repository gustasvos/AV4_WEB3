package com.autobots.automanager.modelos.adicionador;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.entidades.Email;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<Email> {

    @Override
    public void adicionarLink(List<Email> lista) {
        for (Email emailEmail : lista) {
            adicionarLink(emailEmail);
        }
    }

    @Override
    public void adicionarLink(Email objeto) {
        long id = objeto.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmailControle.class)
                        .obterEmail(id))
                .withSelfRel();
        objeto.add(linkProprio);

        Link linkColecao = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmailControle.class)
                        .obterEmails())
                .withRel("emailEmails");

        objeto.add(linkColecao);
    }
}
