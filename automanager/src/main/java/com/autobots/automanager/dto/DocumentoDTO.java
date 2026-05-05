package com.autobots.automanager.dto;

import com.autobots.automanager.enumeracoes.TipoDocumento;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

public record DocumentoDTO(

        Long id,

        @NotNull(message = "Necessário um tipo de documento entre CPF, CNPJ, RG, CNH, PASSAPORTE")
        TipoDocumento tipo,

        @PastOrPresent(message = "A data de emissão não é válida")
        Date dataEmissao,

        @NotBlank(message = "Insira o número do documento")
        String numero
) {
}
