package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.dto.TelefoneDTO;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.atualizador.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class TelefoneServico {

    @Autowired
    private TelefoneRepositorio repositorio;
    @Autowired
    private AdicionadorLinkTelefone adicionadorLinkTelefone;
    @Autowired
    private TelefoneAtualizador atualizador;

    public ResponseEntity<Telefone> obterTelefone(long id) {
        return repositorio.findById(id)
                .map(telefone -> {
                    adicionadorLinkTelefone.adicionarLink(telefone);
                    return ResponseEntity.ok(telefone);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        adicionadorLinkTelefone.adicionarLink(telefones);
        return ResponseEntity.ok(telefones);
    }

    public ResponseEntity<Telefone> cadastrarTelefone(TelefoneDTO dto) {
        Telefone telefone = new Telefone();
        telefone.setDdd(dto.ddd());
        telefone.setNumero(dto.numero());

        Telefone salvo = repositorio.save(telefone);
        adicionadorLinkTelefone.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(TelefoneControle.class)
                        .obterTelefone(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Telefone> atualizarTelefone(long id, TelefoneDTO dto) {
        return repositorio.findById(id)
                .map(telefone -> {
                    atualizador.atualizar(telefone, dto);
                    Telefone salvo = repositorio.save(telefone);
                    adicionadorLinkTelefone.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}