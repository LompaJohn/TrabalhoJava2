package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.PedidoServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PedidoServicoRepository extends JpaRepository<PedidoServico, Long> {
    List<PedidoServico> findByPedidoId(Long id);

    List<PedidoServico> findByServicoId(Long id);

    boolean existsByServicoId(Long id);
}
