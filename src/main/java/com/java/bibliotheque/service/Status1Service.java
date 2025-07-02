package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Status1;
import com.java.bibliotheque.repository.Status1Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class Status1Service {

    private final Status1Repository repository;

    public Status1Service(Status1Repository repository) {
        this.repository = repository;
    }

    public List<Status1> getAll() {
        return repository.findAll();
    }

    public Optional<Status1> getById(Long id) {
        return repository.findById(id);
    }

    public Status1 save(Status1 status) {
        return repository.save(status);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
