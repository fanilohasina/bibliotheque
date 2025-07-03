package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Abonnement;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    @Query("SELECT a FROM Abonnement a WHERE a.user.id = :idUser AND :datePret BETWEEN a.date_debut AND a.date_fin")
    List<Abonnement> findAbonnementActif(@Param("idUser") Long idUser, @Param("datePret") LocalDate datePret);
}
