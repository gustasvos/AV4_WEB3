package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.dto.VendaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VendaServico {

    @Autowired
    private VendaRepositorio repositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private VeiculoRepositorio veiculoRepositorio;
    @Autowired
    private AdicionadorLinkVenda adicionadorLink;
    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;
    @Autowired
    private ServicoRepositorio servicoRepositorio;

    public ResponseEntity<Venda> obterVenda(long id) {
        return repositorio.findById(id)
                .map(venda -> {
                    adicionadorLink.adicionarLink(venda);
                    return ResponseEntity.ok(venda);
                })
                .orElse(ResponseEntity.notFound().build());
//
    }

    public ResponseEntity<List<Venda>> obterVendas() {
        List<Venda> vendas = repositorio.findAll();
        adicionadorLink.adicionarLink(vendas);
        return ResponseEntity.ok(vendas);
//        
    }

    public ResponseEntity<Venda> cadastrarVenda(VendaDTO dto) {
        Venda venda = new Venda();
        venda.setIdentificacao(dto.identificacao());
        venda.setDataCadastro(LocalDateTime.now());

        usuarioRepositorio.findById(dto.clienteId()).ifPresent(venda::setCliente);
        usuarioRepositorio.findById(dto.funcionarioId()).ifPresent(venda::setFuncionario);

        if (dto.veiculoId() != null) {
            veiculoRepositorio.findById(dto.veiculoId()).ifPresent(venda::setVeiculo);
        }

        if (dto.mercadoriaIds() != null) {
            Set<Mercadoria> mercadorias = dto.mercadoriaIds().stream()
                    .map(mercadoriaRepositorio::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            venda.setMercadorias(mercadorias);
        }

        if (dto.servicoIds() != null) {
            Set<Servico> servicos = dto.servicoIds().stream()
                    .map(servicoRepositorio::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());
            venda.setServicos(servicos);
        }

        Venda salvo = repositorio.save(venda);
        adicionadorLink.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VendaControle.class)
                        .obterVenda(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Venda> atualizarVenda(long id, VendaDTO dto) {
        return repositorio.findById(id)
                .map(venda -> {
                    if (dto.identificacao() != null && !dto.identificacao().isBlank()) {
                        venda.setIdentificacao(dto.identificacao());
                    }
                    if (dto.clienteId() != null) {
                        usuarioRepositorio.findById(dto.clienteId())
                                .ifPresent(venda::setCliente);
                    }
                    if (dto.funcionarioId() != null) {
                        usuarioRepositorio.findById(dto.funcionarioId())
                                .ifPresent(venda::setFuncionario);
                    }
                    if (dto.veiculoId() != null) {
                        veiculoRepositorio.findById(dto.veiculoId())
                                .ifPresent(venda::setVeiculo);
                    }

                    Venda salvo = repositorio.save(venda);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirVenda(long id) {
        return repositorio.findById(id)
                .map(venda -> {
                    repositorio.delete(venda);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

}