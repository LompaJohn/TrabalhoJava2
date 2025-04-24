package com.agencia.viagens.sistema.web.dto;

import com.agencia.viagens.sistema.entity.ClienteTipo;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCadastrarDTO {

    @NotBlank(message = "Nome do cliente eh obrigatorio")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "Documento do cliente eh obrigatorio")
    @Size(min = 14, max = 20)
    private String documento;

    @NotBlank(message = "Telefone do cliente eh obrigatorio")
    @Pattern(regexp = "^\\d+$", message = "O telefone deve conter apenas dígitos")
    @Size(max = 20)
    private String telefone;

    @NotBlank(message = "Email do cliente eh obrigatorio")
    @Email(message = "Email invalido")
    @Size(max = 100)
    private String email;

    @NotNull(message = "Tipo do cliente eh obrigatorio")
    private ClienteTipo tipo;
}
