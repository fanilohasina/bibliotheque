package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Livre;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LivreRepository extends JpaRepository<Livre, Long> {
    List<Livre> findByTitreContainingIgnoreCase(String titre);

    List<Livre> findByAuteurNomContainingIgnoreCase(String auteur);

    List<Livre> findByCategoriesNomContainingIgnoreCase(String categorie);
}
