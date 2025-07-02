// AdherentService.java
package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Adherent;
import com.java.bibliotheque.repository.AdherentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdherentService {

    private final AdherentRepository adherentRepository;

    public AdherentService(AdherentRepository adherentRepository) {
        this.adherentRepository = adherentRepository;
    }

    public List<Adherent> getAll() {
        return adherentRepository.findAll();
    }

    public Optional<Adherent> getById(Integer id) {
        return adherentRepository.findById(id);
    }

    public Adherent save(Adherent adherent) {
        return adherentRepository.save(adherent);
    }

    public void delete(Integer id) {
        adherentRepository.deleteById(id);
    }
}
