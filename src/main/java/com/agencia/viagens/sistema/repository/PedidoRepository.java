package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findById(Long id);

    boolean existsByPacoteId(Long pacoteId);

    List<Pedido> findByPacoteId(Long id);

    List<Pedido> findByClienteId(Long id);


    @Query("SELECT DISTINCT o.cliente FROM Pedido o " +
            "WHERE o.pacote.id = :pacoteId")
    Set<Cliente> findClientsByPacoteId(@Param("pacoteId") Long pacoteId);

}
