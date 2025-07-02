package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.StatusReservation;
import com.java.bibliotheque.repository.StatusReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusReservationService {

    private final StatusReservationRepository repository;

    public StatusReservationService(StatusReservationRepository repository) {
        this.repository = repository;
    }

    public List<StatusReservation> findAll() {
        return repository.findAll();
    }

    public Optional<StatusReservation> findById(Integer id) {
        return repository.findById(id);
    }

    public StatusReservation save(StatusReservation statusReservation) {
        return repository.save(statusReservation);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
