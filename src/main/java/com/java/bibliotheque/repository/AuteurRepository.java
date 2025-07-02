package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Auteur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuteurRepository extends JpaRepository<Auteur, Long> {
}
