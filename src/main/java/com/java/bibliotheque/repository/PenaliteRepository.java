package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Penalite;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PenaliteRepository extends JpaRepository<Penalite, Integer> {
    @Query(value = "SELECT * FROM penalite p WHERE p.id_user = :userId AND DATE_ADD(p.date_action, INTERVAL p.nbr_jour DAY) > :date", nativeQuery = true)
    List<Penalite> findPenaliteEnCoursByUser(@Param("userId") Integer userId,
            @Param("date") LocalDate date);

}
