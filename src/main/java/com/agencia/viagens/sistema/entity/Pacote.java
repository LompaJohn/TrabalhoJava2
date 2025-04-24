package com.agencia.viagens.sistema.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name="PacoteViagem")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pacote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "destino")
    private String destino;

    @Column(name = "duracao_dias")
    private int duracaoDias;

    @Column(name = "preco")
    private BigDecimal preco;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "ativo")
    private boolean ativo;
}
