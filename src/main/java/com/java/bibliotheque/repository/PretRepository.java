package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PretRepository extends JpaRepository<Pret, Integer> {
    List<Pret> findByUser(User user);

    List<Pret> findByUserAndLivre_TitreContainingIgnoreCase(User user, String titre);

    @Query("SELECT COALESCE(SUM(p.nbr), 0) FROM Pret p WHERE p.user.id = :idUser AND p.isSurPlace = false")
    int countPretsActifs(@Param("idUser") Long idUser);
}
