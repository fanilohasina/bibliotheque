package com.java.bibliotheque.entite;

import jakarta.persistence.*;

@Entity
@Table(name = "quota")
public class Quota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_quota;

    private Integer nbr_livre_max_pret;
    private Integer nbr_jour_max_pret;
    private Integer nbr_jour_avant_prologement;
    private Integer nbr_prologement_max;
    private Integer nbr_jour_max_prolongement;

    // Getters & Setters

    public Integer getId_quota() {
        return id_quota;
    }

    public void setId_quota(Integer id_quota) {
        this.id_quota = id_quota;
    }

    public Integer getNbr_livre_max_pret() {
        return nbr_livre_max_pret;
    }

    public void setNbr_livre_max_pret(Integer nbr_livre_max_pret) {
        this.nbr_livre_max_pret = nbr_livre_max_pret;
    }

    public Integer getNbr_jour_max_pret() {
        return nbr_jour_max_pret;
    }

    public void setNbr_jour_max_pret(Integer nbr_jour_max_pret) {
        this.nbr_jour_max_pret = nbr_jour_max_pret;
    }

    public Integer getNbr_jour_avant_prologement() {
        return nbr_jour_avant_prologement;
    }

    public void setNbr_jour_avant_prologement(Integer nbr_jour_avant_prologement) {
        this.nbr_jour_avant_prologement = nbr_jour_avant_prologement;
    }

    public Integer getNbr_prologement_max() {
        return nbr_prologement_max;
    }

    public void setNbr_prologement_max(Integer nbr_prologement_max) {
        this.nbr_prologement_max = nbr_prologement_max;
    }

    public Integer getNbr_jour_max_prolongement() {
        return nbr_jour_max_prolongement;
    }

    public void setNbr_jour_max_prolongement(Integer nbr_jour_max_prolongement) {
        this.nbr_jour_max_prolongement = nbr_jour_max_prolongement;
    }

    @Transient
    public String getDescription() {
        return "Livres max: " + nbr_livre_max_pret + ", Jours max: " + nbr_jour_max_pret;
    }

}
