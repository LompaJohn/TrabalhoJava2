package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.PedidoServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PedidoServicoRepository extends JpaRepository<PedidoServico, Long> {
    List<PedidoServico> findByPedidoId(Long id);

    List<PedidoServico> findByServicoId(Long id);

    boolean existsByServicoId(Long id);

    @Modifying
    @Query(value = "INSERT INTO PedidoServico (pedido_id, servico_id, preco_unitario, quantidade) " +
            "VALUES (:pedidoId, :servicoId, :precoUnitario, :quantidade)", nativeQuery = true)
    void salvarRaw(@Param("pedidoId") Long pedidoId,
            @Param("servicoId") Long servicoId,
            @Param("precoUnitario") BigDecimal precoUnitario,
            @Param("quantidade") Long quantidade);
}
