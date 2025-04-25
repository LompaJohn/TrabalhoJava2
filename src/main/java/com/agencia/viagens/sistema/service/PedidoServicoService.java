package com.agencia.viagens.sistema.service;


import com.agencia.viagens.sistema.repository.PedidoServicoRepository;
import lombok.RequiredArgsConstructor;
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


}
