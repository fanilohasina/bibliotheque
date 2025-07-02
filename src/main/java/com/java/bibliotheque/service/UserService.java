package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    public User create(User user) {
        return repo.save(user);
    }

    public User update(Long id, User user) {
        user.setId(id);
        return repo.save(user);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public User findByNomAndPassword(String nom, String password) {
        return repo.findByNomAndPassword(nom, password);
    }

    public Optional<User> findByEmail(String email) {
        return repo.findByEmail(email);
    }
}
