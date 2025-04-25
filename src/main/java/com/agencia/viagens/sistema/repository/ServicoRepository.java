package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findById(Long id);

}
