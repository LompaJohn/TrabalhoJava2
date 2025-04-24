package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Pacote;
import com.agencia.viagens.sistema.repository.PacoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PacoteService {
    private final PacoteRepository repository;


    @Transactional
    public List<Pacote> buscarTodos() {
        return repository.findAll();
    }

    @Transactional
    public Optional<Pacote> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void removerPacote(Pacote pacote) {
        // TODO: ver se o pacote tem clientes relacionados
        throw new RuntimeException("Not implemented yet");
//        repository.delete(pacote);
    }

    @Transactional
    public Pacote salvarPacote(Pacote pacote) {
        return repository.save(pacote);
    }
}
