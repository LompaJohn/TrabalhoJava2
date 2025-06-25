package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Servico;
import com.agencia.viagens.sistema.repository.ServicoRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Transactional
    public List<Servico> buscarPorIds(Collection<Long> ids) {
        return repository.findAllById(ids);
    }

    @Transactional
    public List<Servico> buscar(String query) {
        return repository.buscar(query);
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
    public void removerPorId(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Servico salvarServico(Servico servico) throws ValidationException {
        String nome = servico.getNome().trim();
        if (nome.isEmpty())
            throw new ValidationException("Nome não pode ser vazio!");
        servico.setNome(nome);

        BigDecimal preco = servico.getPreco();

        if (preco.compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Preço não pode ser menor ou igual a zero!");

        return repository.save(servico);
    }
}
