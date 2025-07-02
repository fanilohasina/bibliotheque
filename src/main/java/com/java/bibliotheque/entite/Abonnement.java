package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Abonnement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_abonnement;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    private LocalDate date_debut;
    private LocalDate date_fin;

    // Getters & Setters
    public Long getId_abonnement() {
        return id_abonnement;
    }

    public void setId_abonnement(Long id) {
        this.id_abonnement = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(LocalDate date_debut) {
        this.date_debut = date_debut;
    }

    public LocalDate getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(LocalDate date_fin) {
        this.date_fin = date_fin;
    }
}
