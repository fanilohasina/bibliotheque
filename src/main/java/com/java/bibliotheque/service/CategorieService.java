package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Categorie;
import com.java.bibliotheque.repository.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    private final CategorieRepository repository;

    public CategorieService(CategorieRepository repository) {
        this.repository = repository;
    }

    public List<Categorie> getAll() {
        return repository.findAll();
    }

    public Optional<Categorie> getById(Long id) {
        return repository.findById(id);
    }

    public Categorie create(Categorie cat) {
        return repository.save(cat);
    }

    public Categorie update(Long id, Categorie cat) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setNom(cat.getNom());
                    return repository.save(existing);
                }).orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
