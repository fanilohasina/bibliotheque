package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.StatusReservation;
import com.java.bibliotheque.repository.StatusReservationRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public Map<Integer, StatusReservation> getDerniersStatutsPourReservations(List<Reservation> reservations) {
        Map<Integer, StatusReservation> derniersStatuts = new HashMap<>();

        for (Reservation reservation : reservations) {
            Optional<StatusReservation> dernierStatut = repository
                    .findTopByReservationOrderByDateActionDesc(reservation);

            dernierStatut.ifPresent(stat -> derniersStatuts.put(reservation.getIdReservation(), stat));
        }

        return derniersStatuts;
    }

    public Map<Integer, StatusReservation> getDerniersStatutsParIdPourReservations(List<Reservation> reservations) {
        Map<Integer, StatusReservation> derniersStatuts = new HashMap<>();
    
        for (Reservation reservation : reservations) {
            Optional<StatusReservation> dernierStatut = repository.findTopByReservationOrderByIdStatusReservationDesc(reservation);
    
            dernierStatut.ifPresent(stat -> derniersStatuts.put(reservation.getIdReservation(), stat));
        }
    
        return derniersStatuts;
    }
    

}
