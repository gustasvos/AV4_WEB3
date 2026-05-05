package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkServico;
import com.autobots.automanager.modelos.atualizador.ServicoAtualizador;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class ServicoServico {

    @Autowired
    private ServicoRepositorio repositorio;
    @Autowired
    private AdicionadorLinkServico adicionadorLinkServico;
    @Autowired
    private ServicoAtualizador atualizador;

    public ResponseEntity<Servico> obterServico(long id) {
        return repositorio.findById(id)
                .map(servico -> {
                    adicionadorLinkServico.adicionarLink(servico);
                    return ResponseEntity.ok(servico);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Servico>> obterServicos() {
        List<Servico> servicos = repositorio.findAll();
        adicionadorLinkServico.adicionarLink(servicos);
        return ResponseEntity.ok(servicos);
    }

    public ResponseEntity<Servico> cadastrarServico(ServicoDTO dto) {
        Servico servico = new Servico();
        servico.setNome((dto.nome()));
        servico.setValor(dto.valor());
        servico.setDescricao(dto.descricao());

        Servico salvo = repositorio.save(servico);
        adicionadorLinkServico.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoControle.class)
                        .obterServico(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Servico> atualizarServico(long id, ServicoDTO dto) {
        return repositorio.findById(id)
                .map(servico -> {
                    atualizador.atualizar(servico, dto);
                    Servico salvo = repositorio.save(servico);
                    adicionadorLinkServico.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
