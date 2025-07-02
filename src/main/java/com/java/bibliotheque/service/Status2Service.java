package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Status2;
import com.java.bibliotheque.repository.Status2Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Status2Service {
    private final Status2Repository repository;

    public Status2Service(Status2Repository repository) {
        this.repository = repository;
    }

    public List<Status2> getAll() {
        return repository.findAll();
    }

    public Optional<Status2> getById(Long id) {
        return repository.findById(id);
    }

    public Status2 save(Status2 s) {
        return repository.save(s);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}