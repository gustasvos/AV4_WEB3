package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.dto.VeiculoDTO;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.adicionador.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelos.atualizador.VeiculoAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class VeiculoServico {

    @Autowired
    private VeiculoRepositorio repositorio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;
    @Autowired
    private VeiculoAtualizador atualizador;

    public ResponseEntity<Veiculo> obterVeiculo(long id) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    adicionadorLink.adicionarLink(veiculo);
                    return ResponseEntity.ok(veiculo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Veiculo>> obterVeiculos() {
        List<Veiculo> veiculos = repositorio.findAll();
        adicionadorLink.adicionarLink(veiculos);
        return ResponseEntity.ok(veiculos);
    }

    public ResponseEntity<Veiculo> cadastrarVeiculo(VeiculoDTO dto) {
        Veiculo veiculo = new Veiculo();
        veiculo.setTipo(dto.tipo());
        veiculo.setModelo(dto.modelo());
        veiculo.setPlaca(dto.placa());

        if (dto.proprietarioId() != null) {
            usuarioRepositorio.findById(dto.proprietarioId())
                    .ifPresent(veiculo::setProprietario);
        }

        Veiculo salvo = repositorio.save(veiculo);
        adicionadorLink.adicionarLink(salvo);

        URI location = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(VeiculoControle.class)
                        .obterVeiculo(salvo.getId()))
                .toUri();

        return ResponseEntity.created(location).body(salvo);
    }

    public ResponseEntity<Veiculo> atualizarVeiculo(long id, VeiculoDTO dto) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    atualizador.atualizar(veiculo, dto);
                    if (dto.proprietarioId() != null) {
                        usuarioRepositorio.findById(dto.proprietarioId())
                                .ifPresent(veiculo::setProprietario);
                    }
                    Veiculo salvo = repositorio.save(veiculo);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<Object> excluirVeiculo(long id) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    repositorio.delete(veiculo);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.<Void>notFound().build());
    }

}