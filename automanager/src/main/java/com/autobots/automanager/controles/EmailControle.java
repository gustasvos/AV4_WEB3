package com.autobots.automanager.controles;

import com.autobots.automanager.dto.EmailDTO;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.servicos.EmailServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/emails")
public class EmailControle {

    @Autowired
    private EmailServico servico;

    @GetMapping("/{id}")
    public ResponseEntity<Email> obterEmail(@PathVariable long id) {
        return servico.obterEmail(id);
    }

    @GetMapping
    public ResponseEntity<List<Email>> obterEmails() {
        return servico.obterEmails();
    }

    @PostMapping
    public ResponseEntity<Email> cadastrarEmail(@RequestBody @Valid EmailDTO dto) {
        return servico.cadastrarEmail(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Email> atualizarEmail(
            @PathVariable long id,
            @RequestBody @Valid EmailDTO dto) {
        return servico.atualizarEmail(id, dto);
    }
}
