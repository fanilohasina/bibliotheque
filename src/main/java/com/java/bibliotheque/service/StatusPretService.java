package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.repository.StatusPretRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusPretService {

    private final StatusPretRepository repository;

    public StatusPretService(StatusPretRepository repository) {
        this.repository = repository;
    }

    public List<StatusPret> findAll() {
        return repository.findAll();
    }

    public Optional<StatusPret> findById(Integer id) {
        return repository.findById(id);
    }

    public StatusPret save(StatusPret statusPret) {
        return repository.save(statusPret);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
