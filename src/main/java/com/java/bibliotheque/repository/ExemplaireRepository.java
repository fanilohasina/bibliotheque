package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Exemplaire;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExemplaireRepository extends JpaRepository<Exemplaire, Long> {
}
