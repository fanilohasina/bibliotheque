package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Exemplaire;
import com.java.bibliotheque.repository.ExemplaireRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExemplaireService {

    private final ExemplaireRepository repository;

    public ExemplaireService(ExemplaireRepository repository) {
        this.repository = repository;
    }

    public List<Exemplaire> getAll() {
        return repository.findAll();
    }

    public Optional<Exemplaire> getById(Long id) {
        return repository.findById(id);
    }

    public Exemplaire save(Exemplaire exemplaire) {
        return repository.save(exemplaire);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
