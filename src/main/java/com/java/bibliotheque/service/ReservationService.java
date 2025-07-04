package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository repository;

    public ReservationService(ReservationRepository repository) {
        this.repository = repository;
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public Optional<Reservation> findById(Integer id) {
        return repository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public List<Reservation> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Reservation> findByUserAndLivreTitre(User user, String titre) {
        return repository.findByUserAndLivre_TitreContainingIgnoreCase(user, titre);
    }

    public List<Reservation> findByNomOrDate(String nom, LocalDate date) {
        if (nom != null && date != null) {
            return repository.findByUserNomContainingIgnoreCaseAndDateReservation(nom, date);
        } else if (nom != null) {
            return repository.findByUserNomContainingIgnoreCase(nom);
        } else if (date != null) {
            return repository.findByDateReservation(date);
        }
        return repository.findAll();
    }

}
