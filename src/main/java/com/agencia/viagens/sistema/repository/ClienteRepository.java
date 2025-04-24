package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpfOrPassaporte(String cpf, String passaporte);
}
