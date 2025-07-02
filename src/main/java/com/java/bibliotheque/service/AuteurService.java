package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Auteur;
import com.java.bibliotheque.repository.AuteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuteurService {

    private final AuteurRepository repository;

    public AuteurService(AuteurRepository repository) {
        this.repository = repository;
    }

    public List<Auteur> getAll() {
        return repository.findAll();
    }

    public Optional<Auteur> getById(Long id) {
        return repository.findById(id);
    }

    public void create(Auteur auteur) {
        repository.save(auteur);
    }

    public void update(Long id, Auteur auteur) {
        auteur.setId(id);
        repository.save(auteur);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
