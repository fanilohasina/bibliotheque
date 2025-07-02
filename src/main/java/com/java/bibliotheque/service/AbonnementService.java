package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Abonnement;
import com.java.bibliotheque.repository.AbonnementRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AbonnementService {
    private final AbonnementRepository repository;

    public AbonnementService(AbonnementRepository repository) {
        this.repository = repository;
    }

    public List<Abonnement> getAll() {
        return repository.findAll();
    }

    public Optional<Abonnement> getById(Long id) {
        return repository.findById(id);
    }

    public Abonnement save(Abonnement abonnement) {
        return repository.save(abonnement);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
