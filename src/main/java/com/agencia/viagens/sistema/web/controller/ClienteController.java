package com.agencia.viagens.sistema.web.controller;


import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.ClienteTipo;
import com.agencia.viagens.sistema.service.ClienteService;
import com.agencia.viagens.sistema.web.dto.ClienteCadastrarDTO;
import com.agencia.viagens.sistema.web.validators.ClienteDocumentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import java.util.List;

@RestController
@RequestMapping("api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;
    private final HandlerMapping resourceHandlerMapping;

    @GetMapping("listar")
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }

    @PostMapping("cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody ClienteCadastrarDTO request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setTelefone(String.valueOf(request.getTelefone()));
        cliente.setEmail(request.getEmail());


        ClienteTipo tipo = request.getTipo();
        cliente.setTipo(tipo);

        String documento = request.getDocumento();

        if (!ClienteDocumentoValidator.validate(documento, tipo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Documento invalido!");
        }

        switch (tipo) {
            case NACIONAL -> {
                cliente.setCpf(documento);
            }
            case ESTRANGEIRO -> {
                cliente.setPassaporte(documento);
            }
        }

        // nao permitir cadastrar se o cliente ja existe
        if (clienteService.buscarPorDocumento(documento).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Cliente ja existe!");
        }

        clienteService.salvarCliente(cliente);

        return ResponseEntity.ok("Cliente cadastrado com sucesso!");
    }
}
