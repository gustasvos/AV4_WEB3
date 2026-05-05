package com.autobots.automanager.controles;

import com.autobots.automanager.dto.EnderecoDTO;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.servicos.EnderecoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class EnderecoControle {

    @Autowired
    private EnderecoServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
        return servico.obterEndereco(id);
//
    }

    @GetMapping
    public ResponseEntity<List<Endereco>> obterEnderecos() {
        return servico.obterEnderecos();
//
    }

    @PostMapping
    public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody @Valid EnderecoDTO dto) {
        return servico.cadastrarEndereco(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Endereco> atualizarEndereco(
            @PathVariable long id,
            @RequestBody @Valid EnderecoDTO dto) {
        return servico.atualizarEndereco(id, dto);
    }

}