package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.repository.PenaliteRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PenaliteService {
    private final PenaliteRepository repository;

    public PenaliteService(PenaliteRepository repository) {
        this.repository = repository;
    }

    public List<Penalite> findAll() {
        return repository.findAll();
    }

    public Penalite findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Penalite save(Penalite penalite) {
        return repository.save(penalite);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }
}
