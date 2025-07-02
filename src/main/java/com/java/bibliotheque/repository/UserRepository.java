package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNomAndPassword(String nom, String password);

    Optional<User> findByEmail(String email);
}
