package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.dto.MercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelos.atualizador.MercadoriaAtualizador;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Service
public class MercadoriaServico {

    @Autowired
    private MercadoriaRepositorio repositorio;
    @Autowired
    private AdicionadorLinkMercadoria adicionadorLinkMercadoria;
    @Autowired
    private MercadoriaAtualizador atualizador;

    public ResponseEntity<Mercadoria> obterMercadoria(long id) {
        return repositorio.findById(id)
                .map(mercadoria -> {
                    adicionadorLinkMercadoria.adicionarLink(mercadoria);
                    return ResponseEntity.ok(mercadoria);
                })
                .orElse(ResponseEntity.notFound().build());
//
    }

    public ResponseEntity<List<Mercadoria>> obterMercadorias() {
        List<Mercadoria> mercadorias = repositorio.findAll();
        adicionadorLinkMercadoria.adicionarLink(mercadorias);
        return ResponseEntity.ok(mercadorias);
//        
    }

    public ResponseEntity<Mercadoria> cadastrarMercadoria(MercadoriaDTO dto) {
        Mercadoria mercadoria = new Mercadoria();
        mercadoria.setDataValidade((dto.dataValidade()));
        mercadoria.setDataFabricacao(dto.dataFabricacao());
        mercadoria.setDataCadastro(LocalDate.now());
        mercadoria.setNome(dto.nome());
        mercadoria.setQuantidade(dto.quantidade());
        mercadoria.setValor(dto.valor());
        mercadoria.setDescricao(dto.descricao());

        Mercadoria salvo = repositorio.save(mercadoria);
        adicionadorLinkMercadoria.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaControle.class)
                        .obterMercadoria(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Mercadoria> atualizarMercadoria(long id, MercadoriaDTO dto) {
        return repositorio.findById(id)
                .map(mercadoria -> {
                    atualizador.atualizar(mercadoria, dto);
                    Mercadoria salvo = repositorio.save(mercadoria);
                    adicionadorLinkMercadoria.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
