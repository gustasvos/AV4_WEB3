package com.autobots.automanager.controles;

import com.autobots.automanager.dto.ServicoDTO;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.servicos.ServicoServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/servicos")
public class ServicoControle {

    @Autowired
    private ServicoServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Servico> obterServico(@PathVariable long id) {
        return servico.obterServico(id);
    }

    @GetMapping
    public ResponseEntity<List<Servico>> obterServicos() {
        return servico.obterServicos();
    }

    @PostMapping
    public ResponseEntity<Servico> cadastrarServico(@RequestBody @Valid ServicoDTO dto) {
        return servico.cadastrarServico(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizarServico(
            @PathVariable long id,
            @RequestBody @Valid ServicoDTO dto) {
        return servico.atualizarServico(id, dto);
    }
}
