package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;


    @Transactional
    public List<Cliente> buscarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Optional<Cliente> buscarPorDocumento(String documento) {
        return repository.findByCpfOrPassaporte(documento, documento);
    }

    @Transactional
    public List<Cliente> buscar(String query) {
        return repository.buscar(query);
    }

    @Transactional
    public void removerCliente(Cliente cliente) {
        repository.delete(cliente);
    }

    @Transactional
    public Cliente salvarCliente(Cliente cliente) {
        return repository.save(cliente);
    }
}
