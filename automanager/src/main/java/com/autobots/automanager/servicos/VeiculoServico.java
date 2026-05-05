package com.autobots.automanager.servicos;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.dto.request.VeiculoRequestDTO;
import com.autobots.automanager.dto.response.VeiculoResponseDTO;
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

    public ResponseEntity<VeiculoResponseDTO> obterVeiculo(long id) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    adicionadorLink.adicionarLink(veiculo);
                    return ResponseEntity.ok(toResponseDTO(veiculo));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<VeiculoResponseDTO>> obterVeiculos() {
        List<Veiculo> veiculos = repositorio.findAll();
        adicionadorLink.adicionarLink(veiculos);
        return ResponseEntity.ok(veiculos.stream().map(this::toResponseDTO).toList());
    }

    public ResponseEntity<VeiculoResponseDTO> cadastrarVeiculo(VeiculoRequestDTO dto) {
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

        return ResponseEntity.created(location).body(toResponseDTO(salvo));
    }

    public ResponseEntity<VeiculoResponseDTO> atualizarVeiculo(long id, VeiculoRequestDTO dto) {
        return repositorio.findById(id)
                .map(veiculo -> {
                    atualizador.atualizar(veiculo, dto);

                    if (dto.proprietarioId() != null) {
                        usuarioRepositorio.findById(dto.proprietarioId())
                                .ifPresent(veiculo::setProprietario);
                    }

                    Veiculo salvo = repositorio.save(veiculo);
                    adicionadorLink.adicionarLink(salvo);
                    return ResponseEntity.ok(toResponseDTO(salvo));
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

    private VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {
        return new VeiculoResponseDTO(
                veiculo.getId(),
                veiculo.getTipo(),
                veiculo.getModelo(),
                veiculo.getPlaca(),
                veiculo.getProprietario() != null ? veiculo.getProprietario().getId() : null
        );
    }
}