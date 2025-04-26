package com.agencia.viagens.sistema.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Map;

@Getter
@Setter
public class PedidoCadastrarDTO {
    @NotBlank(message = "Documento do cliente eh obrigatorio")
    @Size(min = 14, max = 20)
    private String clienteDocumento;

    @NotNull(message = "Id do pacote eh obrigatorio")
    private Long pacoteId;

    @NotNull(message = "data da viagem eh obrigatoria")
//    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataViagem;

    @NotNull
    private Map<Long, Long> servicosAdicionaisIdQuant;
}

