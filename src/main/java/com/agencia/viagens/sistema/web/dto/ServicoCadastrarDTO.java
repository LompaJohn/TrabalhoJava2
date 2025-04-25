package com.agencia.viagens.sistema.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ServicoCadastrarDTO {

    @NotBlank(message = "Nome do servico eh obrigatorio")
    @Size(max = 100)
    private String nome;

    @NotNull(message = "Preco eh obrigatorio")
    @PositiveOrZero(message = "Preco tem que ser positivo ou zero")
    private BigDecimal preco;

    @NotBlank(message = "Descricao eh obrigatoria")
    private String descricao;
}
