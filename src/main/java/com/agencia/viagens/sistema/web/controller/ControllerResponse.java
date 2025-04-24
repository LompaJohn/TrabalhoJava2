package com.agencia.viagens.sistema.web.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ControllerResponse {
    private String message;
    private String error;

    ControllerResponse(){
    }

    public ControllerResponse error(String error) {
        this.error = error;
        return this;
    }

    public ControllerResponse message(String message) {
        this.message = message;
        return this;
    }
}
