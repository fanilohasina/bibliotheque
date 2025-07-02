package com.java.bibliotheque.entite;

import jakarta.persistence.*;

@Entity
public class Status2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_status2;

    private String nom;

    // Getters et Setters
    public Long getId_status2() {
        return id_status2;
    }

    public void setId_status2(Long id_status2) {
        this.id_status2 = id_status2;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}