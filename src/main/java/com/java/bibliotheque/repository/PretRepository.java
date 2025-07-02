package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PretRepository extends JpaRepository<Pret, Integer> {
    List<Pret> findByUser(User user);

    List<Pret> findByUserAndLivre_TitreContainingIgnoreCase(User user, String titre);
}
