package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.repository.LivreRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivreService {

    private final LivreRepository repository;

    public LivreService(LivreRepository repository) {
        this.repository = repository;
    }

    public List<Livre> getAll() {
        return repository.findAll();
    }

    public Optional<Livre> getById(Long id) {
        return repository.findById(id);
    }

    public Livre create(Livre livre) {
        return repository.save(livre);
    }

    public Livre update(Long id, Livre livre) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setTitre(livre.getTitre());
                    existing.setDateDePublication(livre.getDateDePublication());
                    existing.setDateDentree(livre.getDateDentree());
                    existing.setAuteur(livre.getAuteur());
                    existing.setCategories(livre.getCategories());
                    existing.setLangue(livre.getLangue());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Livre non trouvé avec id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<Livre> searchByTitre(String titre) {
        return repository.findByTitreContainingIgnoreCase(titre);
    }
}
