package com.java.bibliotheque.entite;

import jakarta.persistence.*;

@Entity
public class Status1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_status1;

    private String nom;

    // Getters et setters
    public Long getId_status1() {
        return id_status1;
    }

    public void setId_status1(Long id_status1) {
        this.id_status1 = id_status1;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
