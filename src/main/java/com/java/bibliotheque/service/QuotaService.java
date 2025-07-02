package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.repository.QuotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuotaService {

    private final QuotaRepository repository;

    public QuotaService(QuotaRepository repository) {
        this.repository = repository;
    }

    public List<Quota> getAll() {
        return repository.findAll();
    }

    public Optional<Quota> getById(Integer id) {
        return repository.findById(id);
    }

    public Quota save(Quota quota) {
        return repository.save(quota);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
