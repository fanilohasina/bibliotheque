package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);

    List<Reservation> findByUserAndLivre_TitreContainingIgnoreCase(User user, String titre);

    List<Reservation> findByUserNomContainingIgnoreCase(String nom);

    List<Reservation> findByDateReservation(LocalDate date);

    List<Reservation> findByUserNomContainingIgnoreCaseAndDateReservation(String nom, LocalDate date);

}
