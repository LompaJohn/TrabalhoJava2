package com.agencia.viagens.sistema.web.dto;

import com.agencia.viagens.sistema.entity.ClienteTipo;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PacoteCadastrarDTO {

    @NotBlank(message = "Nome do pacote eh obrigatorio")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "Duracao em dias eh obrigatoria")
    @Size(max = 100)
    private String destino;

    @NotNull(message = "Duracao em dias eh obrigatoria")
    @Positive(message = "Duracao em dias tem que ser positiva")
    private int duracaoDias;

    @NotNull(message = "Preco eh obrigatorio")
    @Positive(message = "Preco tem que ser positivo")
    private BigDecimal preco;

    @NotBlank(message = "Tipo eh obrigatorio")
    @Size(max = 50)
    private String tipo;

    @NotBlank(message = "Descricao eh obrigatoria")
    private String descricao;
}
