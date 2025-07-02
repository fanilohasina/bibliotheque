package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Quota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotaRepository extends JpaRepository<Quota, Integer> {
    // CRUD standard via JpaRepository
}
