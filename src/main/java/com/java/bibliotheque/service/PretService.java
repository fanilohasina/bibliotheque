package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.PretRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PretService {

    private final PretRepository repository;

    public PretService(PretRepository repository) {
        this.repository = repository;
    }

    public List<Pret> getAll() {
        return repository.findAll();
    }

    public Optional<Pret> getById(Integer id) {
        return repository.findById(id);
    }

    public Pret save(Pret pret) {
        return repository.save(pret);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Pret> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Pret> findByUserAndLivreTitre(User user, String titre) {
        return repository.findByUserAndLivre_TitreContainingIgnoreCase(user, titre);
    }
}
