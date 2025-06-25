package com.agencia.viagens.sistema.service;

import com.agencia.viagens.sistema.entity.Pacote;
import com.agencia.viagens.sistema.repository.PacoteRepository;
import com.agencia.viagens.sistema.repository.PedidoRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public List<Pacote> buscar(String query) {
        return repository.buscar(query);
    }

    @Transactional
    public void removerPacote(Pacote pacote) {
        repository.delete(pacote);
    }

    @Transactional
    public void removerPorId(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void salvarPacote(Pacote pacote) throws ValidationException {
        String nome = pacote.getNome().trim();
        if (nome.isEmpty())
            throw new ValidationException("Nome não pode ser vazio!");
        pacote.setNome(nome);

        String tipo = pacote.getTipo().trim();
        if (tipo.isEmpty())
            throw new ValidationException("Tipo não pode ser vazio!");
        pacote.setTipo(tipo);

        String destino = pacote.getDestino().trim();
        if (destino.isEmpty())
            throw new ValidationException("Destino não pode ser vazio!");
        pacote.setDestino(destino);

        if (pacote.getDuracaoDias() <= 0)
            throw new ValidationException("Duração não pode ser menor ou igual a zero!");

        if (pacote.getPreco().compareTo(BigDecimal.ZERO) <= 0)
            throw new ValidationException("Preço não pode ser menor ou igual a zero!");

        repository.save(pacote);
    }
}
