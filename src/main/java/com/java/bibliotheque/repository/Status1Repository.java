package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Status1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Status1Repository extends JpaRepository<Status1, Long> {
    Status1 findByNom(String nom);
}
