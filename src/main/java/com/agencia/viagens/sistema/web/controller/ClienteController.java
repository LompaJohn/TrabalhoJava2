package com.agencia.viagens.sistema.web.controller;


import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {
    private final ClienteService clienteService;

//    @PostMapping
//    public ResponseEntity<ClienteResponseDTO> create(@Valid @RequestBody ClienteCreateDTO createDTO, @AuthenticationPrincipal JwtUserDetails userDetails) {
//        Cliente cliente = ClienteMapper.toCliente(createDTO);
//        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
//        clienteService.salvar(cliente);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toDTO(cliente));
//    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getAll() {
//        return ResponseEntity.ok(PageableMapper.toDTO(clienteService.buscarTodos(pageable)));
        return ResponseEntity.ok(clienteService.buscarTodos());
    }
}
