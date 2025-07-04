package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.PenaliteRepository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Penalite> getPenalitesByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Penalite> getPenalitesParRecherche(String etudiant, LocalDate dateRecherche) {
        boolean filtreEtudiant = etudiant != null && !etudiant.isEmpty();
        boolean filtreDate = dateRecherche != null;

        if (!filtreEtudiant && !filtreDate) {
            return repository.findAll();
        }
        if (filtreEtudiant && filtreDate) {
            return repository.findByEtudiantAndDateRecherche(etudiant, dateRecherche);
        }
        if (filtreEtudiant) {
            // Recherche par étudiant seul
            return repository.findByUserNomContainingIgnoreCase(etudiant);
        }
        if (filtreDate) {
            return repository.findByDateRecherche(dateRecherche);
        }

        return List.of();
    }

}
