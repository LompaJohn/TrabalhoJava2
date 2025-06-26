package com.agencia.viagens.sistema.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "pacote_id")
    private Pacote pacote;

    @Column(name = "data_contratacao")
    private LocalDate dataContratacao;

    @Column(name = "data_viagem")
    private LocalDate dataViagem;

    @Column(name = "valor_total")
    private BigDecimal valorTotal;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PedidoServico> servicos = new HashSet<>();

    public void addServico(PedidoServico servico) {
        servicos.add(servico);
        servico.setPedido(this);
    }
}
