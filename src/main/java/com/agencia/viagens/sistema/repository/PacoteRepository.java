package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PacoteRepository extends JpaRepository<Pacote, Long> {
    Optional<Pacote> findById(Long id);
}
