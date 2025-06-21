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
            "c.nome LIKE %:query% OR " +
            "c.email LIKE %:query% OR " +
            "c.telefone LIKE %:query% OR " +
            "c.cpf LIKE %:query%")
    List<Cliente> buscar(@Param("query") String query);

}
