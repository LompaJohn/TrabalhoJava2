package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository repository;


    @Transactional
    public List<Cliente> buscarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Cliente buscarPorDocumento(String documento) {
        return repository.findByCpfOrPassaporte(documento, documento).orElseThrow(() -> new RuntimeException("cliente nao encontrado"));
    }
}
