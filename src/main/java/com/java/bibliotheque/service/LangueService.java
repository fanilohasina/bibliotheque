package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Langue;
import com.java.bibliotheque.repository.LangueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LangueService {
    private final LangueRepository repository;

    public LangueService(LangueRepository repository) {
        this.repository = repository;
    }

    public List<Langue> getAll() {
        return repository.findAll();
    }

    public Optional<Langue> getById(Long id) {
        return repository.findById(id);
    }

    public Langue create(Langue langue) {
        return repository.save(langue);
    }

    public Langue update(Long id, Langue langue) {
        return repository.findById(id).map(l -> {
            l.setNom(langue.getNom());
            return repository.save(l);
        }).orElseThrow(() -> new RuntimeException("Langue non trouvée"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
