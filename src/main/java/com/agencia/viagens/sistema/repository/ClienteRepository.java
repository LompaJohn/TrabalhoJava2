package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpfOrPassaporte(String cpf, String passaporte);

    @Query("SELECT c FROM Cliente c WHERE " +
            "CAST(c.id AS string) LIKE CONCAT('%', :query, '%') OR " +
            "c.nome LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.email LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.telefone LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "c.cpf LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Cliente> buscar(@Param("query") String query);

}
