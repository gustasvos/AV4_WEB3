package com.autobots.automanager.modelos.adicionador;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entidades.Servico;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {

    @Override
    public void adicionarLink(List<Servico> lista) {
        for (Servico servico : lista) {
            adicionarLink(servico);
        }
    }

    @Override
    public void adicionarLink(Servico objeto) {
        long id = objeto.getId();

        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .obterServico(id))
                .withSelfRel();
        objeto.add(linkProprio);

        Link linkColecao = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .obterServicos())
                .withRel("servicos");

        objeto.add(linkColecao);
    }
}
