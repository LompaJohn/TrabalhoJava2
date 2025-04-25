package com.agencia.viagens.sistema.web.controller;


import com.agencia.viagens.sistema.entity.Servico;
import com.agencia.viagens.sistema.service.PedidoServicoService;
import com.agencia.viagens.sistema.service.ServicoService;
import com.agencia.viagens.sistema.web.dto.ServicoCadastrarDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/servicos")
@RequiredArgsConstructor
public class ServicoController {
    private final ServicoService servicoService;
    private final PedidoServicoService pedidoServicoService;

    @GetMapping("listar")
    public ResponseEntity<List<Servico>> listar() {
        return ResponseEntity.ok(servicoService.buscarTodos());
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {

        Optional<Servico> servico = servicoService.buscarPorId(id);

        if (servico.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(servico.get());
    }

    @PostMapping("remover/{id}")
    public ResponseEntity<ControllerResponse> remover(@PathVariable Long id) {
        Optional<Servico> servico = servicoService.buscarPorId(id);

        if (servico.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ControllerResponse().error("Servico nao encontrado!"));
        }

        if (pedidoServicoService.existePorServicoId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ControllerResponse().error("Servico nao pode ser removido, existem pedidos dependentes!"));
        }

        servicoService.removerServico(servico.get());
        return ResponseEntity.ok(new ControllerResponse().message("Servico removido com sucesso!"));
    }

    @PostMapping("cadastrar")
    public ResponseEntity<ControllerResponse> cadastrar(@RequestBody ServicoCadastrarDTO request) {
        Servico servico = new Servico();

        servico.setNome(request.getNome());
        servico.setPreco(request.getPreco());
        servico.setDescricao(request.getDescricao());
        servico.setAtivo(true);

        servicoService.salvarServico(servico);

        return ResponseEntity.ok(new ControllerResponse().message("Servico cadastrado com sucesso!"));
    }
}
