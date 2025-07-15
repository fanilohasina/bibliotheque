package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.ValeurPenalite;
import com.java.bibliotheque.repository.ValeurPenaliteRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValeurPenaliteService {

    private final ValeurPenaliteRepository repository;

    public ValeurPenaliteService(ValeurPenaliteRepository repository) {
        this.repository = repository;
    }

    public List<ValeurPenalite> getAll() {
        return repository.findAll();
    }

    public Optional<ValeurPenalite> getById(Integer id) {
        return repository.findById(id);
    }

    public ValeurPenalite save(ValeurPenalite valeurPenalite) {
        return repository.save(valeurPenalite);
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    public int getCoutPourJours(int jours) throws Exception {
        return repository.findByJourInInterval(jours)
                .map(ValeurPenalite::getCout)
                .orElseThrow(() -> new Exception("Aucune pénalité définie pour " + jours + " jours de retard."));
    }

}
