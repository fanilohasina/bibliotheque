package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Abonnement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
}
