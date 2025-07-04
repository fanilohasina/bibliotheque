package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {

    private final UserRepository repo;
    private final BCryptPasswordEncoder passwordEncoder;

    // Injection du repo + passwordEncoder via constructeur
    public UserService(UserRepository repo, BCryptPasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    public User create(User user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return repo.save(user);
    }

    public User update(Long id, User user) {
        user.setId(id);
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

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

    public User authenticate(String nom, String rawPassword) {
        System.out.println("Recherche utilisateur pour nom = " + nom);
        Optional<User> userOpt = repo.findByNom(nom);
        if (!userOpt.isPresent()) {
            System.out.println("Utilisateur non trouvé");
            return null;
        }
        User user = userOpt.get();
        System.out.println("Utilisateur trouvé, vérification mot de passe...");
        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("Correspondance mot de passe : " + matches);
        return matches ? user : null;
    }

    public List<User> getAllExceptAdmin() {
        return repo.findByAdherentNomNotIgnoreCase("Admin");
    }

}
