package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Status2;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Status2Repository extends JpaRepository<Status2, Long> {
    Status2 findByNom(String nom);
}
