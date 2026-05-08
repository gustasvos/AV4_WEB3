package com.autobots.automanager.modelos.adicionador;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entidades.Venda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLinkMercadoria;
    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;
    @Autowired
    private AdicionadorLinkVeiculo adicionadorLinkVeiculo;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLinkUsuario;

    @Override
    public void adicionarLink(Venda objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .obterVenda(objeto.getId()))
                .withSelfRel();
        objeto.add(linkProprio);

        Link linkColecao = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .obterVendas())
                .withRel("vendas");
        objeto.add(linkColecao);

        Link linkCliente = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuario(objeto.getCliente().getId()))
                .withRel("cliente");
        objeto.add(linkCliente);

        Link linkFuncionario = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuario(objeto.getFuncionario().getId()))
                .withRel("funcionario");
        objeto.add(linkFuncionario);

        if (objeto.getVeiculo() != null) {
            Link linkVeiculo = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(VeiculoControle.class)
                            .obterVeiculo(objeto.getVeiculo().getId()))
                    .withRel("veiculo");
            objeto.add(linkVeiculo);
        }

        if (objeto.getMercadorias() != null) {
            adicionadorLinkMercadoria.adicionarLink(objeto.getMercadorias());
        }
        if (objeto.getServicos() != null) {
            adicionadorLinkServico.adicionarLink(objeto.getServicos());
        }

    }
}