package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Exemplaire;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
    @Query("SELECT e.livre.id, SUM(CASE WHEN e.action = true THEN e.nbr ELSE -e.nbr END) " +
            "FROM Exemplaire e GROUP BY e.livre.id")
    List<Object[]> getExemplaireCountPerLivre();

    @Query("""
                SELECT
                    COALESCE(SUM(CASE WHEN e.action = true THEN e.nbr ELSE -e.nbr END), 0)
                FROM Exemplaire e
                WHERE e.livre.id = :idLivre
            """)
    int totalStockDisponible(@Param("idLivre") Long idLivre);
}
