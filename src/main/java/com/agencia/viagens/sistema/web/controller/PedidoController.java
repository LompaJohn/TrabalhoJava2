package com.agencia.viagens.sistema.web.controller;

import com.agencia.viagens.sistema.entity.*;
import com.agencia.viagens.sistema.service.*;
import com.agencia.viagens.sistema.web.dto.PedidoCadastrarDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/v1/pedido")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final PacoteService pacoteService;
    private final ServicoService servicoService;
    private final PedidoServicoService pedidoServicoService;

    @GetMapping("listar")
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.buscarTodos());
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {

        Optional<Pedido> pedido = pedidoService.buscarPorId(id);

        if (pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(pedido.get());
    }

    @PostMapping("remover/{id}")
    public ResponseEntity<ControllerResponse> remover(@PathVariable Long id) {

        Optional<Pedido> pedido = pedidoService.buscarPorId(id);

        if (pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ControllerResponse().error("Pedido nao encontrado!"));
        }

        pedidoService.removerPedido(pedido.get());
        return ResponseEntity.ok(new ControllerResponse().message("Pedido removido com sucesso!"));
    }

    @Transactional
    @PostMapping("cadastrar")
    public ResponseEntity<ControllerResponse> cadastrar(@RequestBody PedidoCadastrarDTO request) {

        Optional<Cliente> cliente = clienteService.buscarPorDocumento(request.getClienteDocumento());
        if (cliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ControllerResponse().error("Cliente nao encontrado!"));
        }
        Optional<Pacote> pacote = pacoteService.buscarPorId(request.getPacoteId());
        if (pacote.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ControllerResponse().error("Pacote nao encontrado!"));
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente.get());
        pedido.setPacote(pacote.get());

        pedido.setDataContratacao(LocalDate.now());
        pedido.setDataViagem(request.getDataViagem());

        BigDecimal valorTotal = pacote.get().getPreco();

        Map<Long, Long> adicionaisIdQuant = request.getServicosAdicionaisIdQuant();

        if (!adicionaisIdQuant.isEmpty()) {
            Set<Long> adicionaisIdSet = adicionaisIdQuant.keySet();
            List<Servico> servicos = servicoService.buscarPorIds(adicionaisIdSet);

            if (adicionaisIdSet.size() != servicos.size()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ControllerResponse().error("Lista de id de servico nao valida, algum id eh invalido!"));
            }

            for (Servico servico : servicos) {
                Long quantidade = adicionaisIdQuant.get(servico.getId());
                BigDecimal precoTotalServico = servico.getPreco().multiply(BigDecimal.valueOf(quantidade));
                valorTotal = valorTotal.add(precoTotalServico);

                PedidoServico pedidoServico = new PedidoServico();
                pedidoServico.setServico(servico);
                pedidoServico.setPrecoUnitario(servico.getPreco());
                pedidoServico.setQuantidade(quantidade);

                pedido.addServico(pedidoServico);
            }
        }

        pedido.setValorTotal(valorTotal);

        pedidoService.salvarPedido(pedido);
        return ResponseEntity.ok(new ControllerResponse().message("Pedido cadastrado com sucesso!"));
    }
}
