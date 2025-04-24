package com.agencia.viagens.sistema.web.dto;

import com.agencia.viagens.sistema.entity.ClienteTipo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteCadastrarDTO {

    @NotBlank(message = "Nome do cliente eh obrigatorio")
    private String nome;

    @NotBlank(message = "Documento do cliente eh obrigatorio")
    private String documento;

    @NotBlank(message = "Telefone do cliente eh obrigatorio")
    @Pattern(regexp = "^\\d+$", message = "O telefone deve conter apenas dígitos")
    private String telefone;

    @NotBlank(message = "Email do cliente eh obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    @NotNull(message = "Tipo do cliente eh obrigatorio")
    private ClienteTipo tipo;
}
