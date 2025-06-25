package com.agencia.viagens.sistema.repository;

import com.agencia.viagens.sistema.entity.Pacote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacoteRepository extends JpaRepository<Pacote, Long> {
    Optional<Pacote> findById(Long id);

    @Query("SELECT p FROM Pacote p WHERE " +
            "CAST(p.id AS string) LIKE CONCAT('%', :query, '%') OR " +
            "LOWER(p.nome) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.destino) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.tipo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.descricao) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(p.duracaoDias AS string) LIKE CONCAT('%', :query, '%') OR " +
            "CAST(p.preco AS string) LIKE CONCAT('%', :query, '%')")
    List<Pacote> buscar(@Param("query") String query);

}
