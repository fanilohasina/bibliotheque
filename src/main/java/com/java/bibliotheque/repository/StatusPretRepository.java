package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.StatusPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPretRepository extends JpaRepository<StatusPret, Integer> {
    // Pas besoin de méthode supplémentaire pour CRUD basique
}
