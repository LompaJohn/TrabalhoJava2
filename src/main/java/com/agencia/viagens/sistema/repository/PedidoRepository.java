package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Cliente;
import com.agencia.viagens.sistema.entity.Pedido;
import com.agencia.viagens.sistema.entity.Servico;

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

    @Query("SELECT p FROM Pedido p " +
            "LEFT JOIN p.cliente c " +
            "LEFT JOIN p.pacote pa " +
            "WHERE " +
            "CAST(p.id AS string) LIKE %:query% OR " +
            "LOWER(p.status) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(p.valorTotal AS string) LIKE %:query% OR " +
            "CAST(p.dataContratacao AS string) LIKE %:query% OR " +
            "CAST(p.dataViagem AS string) LIKE %:query% OR " +
            "LOWER(c.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.cpf) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(c.passaporte) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(pa.nome) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Pedido> buscar(@Param("query") String query);

    @Query("SELECT DISTINCT o.cliente FROM Pedido o " +
            "WHERE o.pacote.id = :pacoteId")

    Set<Cliente> findClientsByPacoteId(@Param("pacoteId") Long pacoteId);
}
