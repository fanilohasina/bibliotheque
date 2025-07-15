package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.StatusReservation;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusReservationRepository extends JpaRepository<StatusReservation, Integer> {
    Optional<StatusReservation> findTopByReservationOrderByDateActionDesc(Reservation reservation);
    Optional<StatusReservation> findTopByReservationOrderByIdStatusReservationDesc(Reservation reservation);


    List<StatusReservation> findByReservationOrderByDateActionAsc(Reservation reservation);

}
