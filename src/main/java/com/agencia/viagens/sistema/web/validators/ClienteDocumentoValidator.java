package com.agencia.viagens.sistema.web.validators;

import com.agencia.viagens.sistema.entity.ClienteTipo;

import java.util.regex.Pattern;

public class ClienteDocumentoValidator {
    private static final Pattern pattern = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$");

    public static boolean validate(String documento, ClienteTipo tipo) {
        boolean cpf_valido =
         pattern.matcher(documento).matches();


        switch (tipo) {
            case NACIONAL -> {
                return cpf_valido;
            }
            case ESTRANGEIRO -> {
                // evitar ter cpfs nos passaportes pro findByCpfOrPassaporte nunca dar erro
                return documento.length() > 5 && !cpf_valido;
            }
            default -> {
                throw new IllegalArgumentException("Tipo invalido");
            }
        }
    }
}
