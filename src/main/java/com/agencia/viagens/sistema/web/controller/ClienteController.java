package com.agencia.viagens.sistema.web.controller;


import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.ClienteTipo;
import com.agencia.viagens.sistema.service.ClienteService;
import com.agencia.viagens.sistema.web.dto.ClienteCadastrarDTO;
import com.agencia.viagens.sistema.web.validators.ClienteDocumentoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

    @GetMapping("listar")
    public ResponseEntity<List<Cliente>> listar() {
        return ResponseEntity.ok(clienteService.buscarTodos());
    }


    @GetMapping("buscar/{documento}")
    public ResponseEntity<Cliente> buscarPorDocumento(@PathVariable String documento) {

        Optional<Cliente> cliente = clienteService.buscarPorDocumento(documento);

        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(cliente.get());
    }

    @PostMapping("remover/{documento}")
    public ResponseEntity<ControllerResponse> remover(@PathVariable String documento) {

        Optional<Cliente> cliente = clienteService.buscarPorDocumento(documento);

        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ControllerResponse().error("Cliente nao encontrado!"));
        }

        clienteService.removerCliente(cliente.get());
        return ResponseEntity.ok(new ControllerResponse().message("Cliente removido com sucesso!"));
    }

    @PostMapping("cadastrar")
    public ResponseEntity<ControllerResponse> cadastrar(@RequestBody ClienteCadastrarDTO request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setTelefone(request.getTelefone());
        cliente.setEmail(request.getEmail());


        ClienteTipo tipo = request.getTipo();
        cliente.setTipo(tipo);

        String documento = request.getDocumento();

        if (!ClienteDocumentoValidator.validate(documento, tipo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ControllerResponse().error("Documento invalido!"));
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ControllerResponse().error("Cliente ja existe!"));
        }

        clienteService.salvarCliente(cliente);

        return ResponseEntity.ok(new ControllerResponse().message("Cliente cadastrado com sucesso!"));
    }
}
