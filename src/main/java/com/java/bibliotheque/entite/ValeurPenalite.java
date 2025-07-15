package com.java.bibliotheque.entite;

import jakarta.persistence.*;

@Entity
@Table(name = "valeur_penalite")
public class ValeurPenalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private int jourInf;
    private int jourSup;
    private int cout;

    // Getters & Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getJourInf() {
        return jourInf;
    }

    public void setJourInf(int jourInf) {
        this.jourInf = jourInf;
    }

    public int getJourSup() {
        return jourSup;
    }

    public void setJourSup(int jourSup) {
        this.jourSup = jourSup;
    }

    public int getCout() {
        return cout;
    }

    public void setCout(int cout) {
        this.cout = cout;
    }
}
