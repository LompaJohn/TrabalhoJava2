package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface PacoteRepository extends JpaRepository<Pacote, Long> {
    Optional<Pacote> findById(Long id);

    List<Pacote> findByIdOrNomeOrDestinoOrDuracaoDiasOrPrecoOrTipoOrDescricao(Long id, String nome, String destino, int duracaoDias, BigDecimal preco, String tipo, String descricao);


}
