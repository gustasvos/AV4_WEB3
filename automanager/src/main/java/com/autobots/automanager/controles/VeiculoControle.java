package com.autobots.automanager.controles;

import com.autobots.automanager.dto.VeiculoDTO;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.servicos.VeiculoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoControle {

    @Autowired
    private VeiculoServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
        return servico.obterVeiculo(id);
    }

    @GetMapping
    public ResponseEntity<List<Veiculo>> obterVeiculos() {
        return servico.obterVeiculos();
    }

    @PostMapping
    public ResponseEntity<Veiculo> cadastrarVeiculo(@RequestBody @Valid VeiculoDTO dto) {
        return servico.cadastrarVeiculo(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> atualizarVeiculo(
            @PathVariable long id,
            @RequestBody @Valid VeiculoDTO dto) {
        return servico.atualizarVeiculo(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirVeiculo(@PathVariable long id) {
        return servico.excluirVeiculo(id);
//
    }
}