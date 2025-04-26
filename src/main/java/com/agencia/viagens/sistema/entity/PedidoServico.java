package com.agencia.viagens.sistema.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "PedidoServico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoServico {
    @EmbeddedId
    private PedidoServicoId id = new PedidoServicoId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId")
    @JsonIgnore
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @MapsId("servicoId")
    @JoinColumn(name = "servico_id")
    private Servico servico;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    @Column(name = "quantidade")
    private Long quantidade;

}
