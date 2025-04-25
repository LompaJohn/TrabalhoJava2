package com.agencia.viagens.sistema.service;


import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.Pedido;
import com.agencia.viagens.sistema.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository repository;

    @Transactional
    public List<Pedido> buscarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Optional<Pedido> buscarPorId(Long id) {
        return repository.findById(id);
    }


    @Transactional
    public Set<Cliente> buscarClientesPorPacoteId(Long pacoteId) {
        return repository.findClientsByPacoteId(pacoteId);
    }


    @Transactional
    public List<Pedido> buscarPorClienteId(Long id) {
        return repository.findByPacoteId(id);
    }

    @Transactional
    public boolean existePorPacoteId(Long pacoteId) {
        return repository.existsByPacoteId(pacoteId);
    }


    @Transactional
    public void removerPedido(Pedido pedido) {
        repository.delete(pedido);
    }

    @Transactional
    public Pedido salvarPedido(Pedido pedido) {
        return repository.save(pedido);
    }
}
