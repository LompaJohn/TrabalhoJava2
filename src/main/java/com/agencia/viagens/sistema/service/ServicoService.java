package com.agencia.viagens.sistema.service;


import com.agencia.viagens.sistema.entity.Servico;
import com.agencia.viagens.sistema.repository.ServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicoService {
    private final ServicoRepository repository;

    @Transactional
    public List<Servico> buscarTodos() {
        return repository.findAll();
    }

    public List<Servico> buscarPorIds(Collection<Long> ids) {
        return repository.findAllById(ids);
    }

    @Transactional
    public Optional<Servico> buscarPorId(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public void removerServico(Servico servico) {
        repository.delete(servico);
    }

    @Transactional
    public Servico salvarServico(Servico servico) {
        return repository.save(servico);
    }
}
