package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Pret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPret;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_livre", nullable = false)
    private Livre livre;

    private Integer nbr;

    private Boolean isSurPlace;

    private LocalDate datePret;

    // Getters et Setters

    public Integer getId_pret() {
        return idPret;
    }

    public Integer getIdPret() {
        return idPret;
    }

    public void setId_pret(Integer id_pret) {
        this.idPret = id_pret;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Integer getNbr() {
        return nbr;
    }

    public void setNbr(Integer nbr) {
        this.nbr = nbr;
    }

    public Boolean getIsSurPlace() {
        return isSurPlace;
    }

    public void setIsSurPlace(Boolean isSurPlace) {
        this.isSurPlace = isSurPlace;
    }

    public LocalDate getDatePret() {
        return datePret;
    }

    public void setDatePret(LocalDate datePret) {
        this.datePret = datePret;
    }
}
