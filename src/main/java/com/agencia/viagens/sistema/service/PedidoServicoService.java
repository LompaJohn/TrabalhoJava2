package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.PedidoServico;
import com.agencia.viagens.sistema.repository.PedidoServicoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PedidoServicoService {
    private final PedidoServicoRepository repository;

    @Transactional
    public boolean existePorServicoId(Long servicoId) {
        return repository.existsByServicoId(servicoId);
    }

    @Transactional
    public void salvar(PedidoServico pedidoServico) {
        repository.save(pedidoServico);
    }

    @Transactional
    public void salvarRaw(PedidoServico pedidoServico) {
        repository.salvarRaw(pedidoServico.getPedido().getId(), pedidoServico.getServico().getId(),
                pedidoServico.getPrecoUnitario(), pedidoServico.getQuantidade());
    }

    @Transactional
    public List<PedidoServico> buscarPorPedidoId(Long pedidoId) {
        return repository.findByPedidoId(pedidoId);
    }

}
