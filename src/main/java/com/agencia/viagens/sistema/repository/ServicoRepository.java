package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    Optional<Servico> findById(Long id);

    @Query("SELECT p FROM Servico p WHERE " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.descricao) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(p.preco AS string) LIKE CONCAT('%', :query, '%') OR " +
            "CAST(p.id AS string) LIKE CONCAT('%', :query, '%')")
    List<Servico> buscar(@Param("query") String query);

}
