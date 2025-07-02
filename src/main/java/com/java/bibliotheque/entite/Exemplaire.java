package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Exemplaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_exemplaire;

    @ManyToOne
    @JoinColumn(name = "id_livre")
    private Livre livre;

    private LocalDate dateAction;
    private Boolean action;
    private Integer nbr;

    // Getters & Setters
    public Long getId_exemplaire() {
        return id_exemplaire;
    }

    public void setId_exemplaire(Long id_exemplaire) {
        this.id_exemplaire = id_exemplaire;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public LocalDate getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDate dateAction) {
        this.dateAction = dateAction;
    }

    public Boolean getAction() {
        return action;
    }

    public void setAction(Boolean action) {
        this.action = action;
    }

    public Integer getNbr() {
        return nbr;
    }

    public void setNbr(Integer nbr) {
        this.nbr = nbr;
    }
}
