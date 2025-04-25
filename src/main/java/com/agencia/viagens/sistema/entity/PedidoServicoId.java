package com.agencia.viagens.sistema.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class PedidoServicoId implements Serializable {
    private Long pedidoId;
    private Long servicoId;
}
