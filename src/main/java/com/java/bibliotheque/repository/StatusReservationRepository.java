package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.StatusReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusReservationRepository extends JpaRepository<StatusReservation, Integer> {
}
