package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.ValeurPenalite;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ValeurPenaliteRepository extends JpaRepository<ValeurPenalite, Integer> {
    @Query("SELECT v FROM ValeurPenalite v WHERE :jours BETWEEN v.jourInf AND v.jourSup")
    Optional<ValeurPenalite> findByJourInInterval(@Param("jours") int jours);

}
