package com.agencia.viagens.sistema.web.validators;

import com.agencia.viagens.sistema.entity.ClienteTipo;

import java.util.regex.Pattern;

public class ClienteDocumentoValidator {
    private static final Pattern pattern = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$");

    public static boolean validate(String documento, ClienteTipo tipo) {
        switch (tipo) {
            case NACIONAL -> {
                return pattern.matcher(documento).matches();
            }
            case ESTRANGEIRO -> {
                return documento.length() > 5;
            }
            default -> {
                throw new IllegalArgumentException("Tipo invalido");
            }
        }
    }
}
