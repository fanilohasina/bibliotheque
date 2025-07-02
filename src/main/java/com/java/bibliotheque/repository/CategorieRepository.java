package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {
}
