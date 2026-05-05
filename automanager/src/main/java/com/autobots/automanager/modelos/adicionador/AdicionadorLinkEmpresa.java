package com.autobots.automanager.modelos.adicionador;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {

    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;
    @Autowired
    private AdicionadorLinkEndereco adicionadorLinkEndereco;
    @Autowired
    private AdicionadorLinkUsuario adicionadorLinkUsuario;
    @Autowired
    private AdicionadorLinkMercadoria adicionadorLinkMercadoria;
    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;
    @Autowired
    private AdicionadorLinkVenda adicionadorLinkVenda;

    @Override
    public void adicionarLink(Empresa objeto) {
        Link selfLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .obterEmpresa(objeto.getId()))
                .withSelfRel();
        objeto.add(selfLink);

        Link colecaoLink = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(EmpresaControle.class)
                        .obterEmpresas())
                .withRel("empresas");
        objeto.add(colecaoLink);

        if (objeto.getEndereco() != null) {
            adicionadorLinkEndereco.adicionarLink(objeto.getEndereco());
        }
        if (objeto.getTelefones() != null) {
            adicionadorLinkTelefone.adicionarLink(objeto.getTelefones());
        }
        if (objeto.getUsuarios() != null) {
            adicionadorLinkUsuario.adicionarLink(objeto.getUsuarios());
        }
        if (objeto.getMercadorias() != null) {
            adicionadorLinkMercadoria.adicionarLink(objeto.getMercadorias());
        }
        if (objeto.getServicos() != null) {
            adicionadorLinkServico.adicionarLink(objeto.getServicos());
        }
        if (objeto.getVendas() != null) {
            adicionadorLinkVenda.adicionarLink(objeto.getVendas());
        }
    }
}