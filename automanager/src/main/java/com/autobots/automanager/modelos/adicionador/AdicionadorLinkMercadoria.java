package com.autobots.automanager.modelos.adicionador;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            adicionarLink(mercadoria);
        }
    }

    @Override
    public void adicionarLink(Mercadoria objeto) {
        long id = objeto.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaControle.class)
                        .obterMercadoria(id))
                .withSelfRel();
        objeto.add(linkProprio);

        Link linkColecao = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaControle.class)
                        .obterMercadorias())
                .withRel("mercadorias");

        objeto.add(linkColecao);
    }
}
