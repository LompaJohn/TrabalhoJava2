package com.agencia.viagens.sistema.web.controller;


import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.Pacote;
import com.agencia.viagens.sistema.service.PacoteService;
import com.agencia.viagens.sistema.service.PedidoService;
import com.agencia.viagens.sistema.service.ServicoService;
import com.agencia.viagens.sistema.web.dto.PacoteCadastrarDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/pacotes")
@RequiredArgsConstructor
public class PacoteController {
    private final PacoteService pacoteService;
    private final PedidoService pedidoService;
    private final ServicoService servicoService;

    @GetMapping("listar")
    public ResponseEntity<List<Pacote>> listar() {
        return ResponseEntity.ok(pacoteService.buscarTodos());
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<Pacote> buscarPorId(@PathVariable Long id) {

        Optional<Pacote> pacote = pacoteService.buscarPorId(id);

        if (pacote.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(pacote.get());
    }

    @PostMapping("remover/{id}")
    public ResponseEntity<ControllerResponse> remover(@PathVariable Long id) {


        if (pedidoService.existePorPacoteId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ControllerResponse().error("Pacote nao pode ser removido, pedidos dependem dele!"));
        }

        Optional<Pacote> pacote = pacoteService.buscarPorId(id);

        if (pacote.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ControllerResponse().error("Pacote nao encontrado!"));
        }


        pacoteService.removerPacote(pacote.get());
        return ResponseEntity.ok(new ControllerResponse().message("Pacote removido com sucesso!"));
    }

    @GetMapping("listar_clientes/{id}")
    public ResponseEntity<Set<Cliente>> listar_clientes(@PathVariable Long id) {
        return ResponseEntity.ok().body(pedidoService.buscarClientesPorPacoteId(id));

    }

    @PostMapping("cadastrar")
    public ResponseEntity<ControllerResponse> cadastrar(@RequestBody PacoteCadastrarDTO request) {
        Pacote pacote = new Pacote();
        pacote.setNome(request.getNome());
        pacote.setDestino(request.getDestino());
        pacote.setDuracaoDias(request.getDuracaoDias());
        pacote.setPreco(request.getPreco());
        pacote.setTipo(request.getTipo());
        pacote.setDescricao(request.getDescricao());
        pacote.setAtivo(true);

        pacoteService.salvarPacote(pacote);

        return ResponseEntity.ok(new ControllerResponse().message("Pacote cadastrado com sucesso!"));
    }
}
