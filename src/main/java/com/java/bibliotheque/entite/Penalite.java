package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Penalite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPenalite;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    private LocalDate dateAction;

    private int nbrJour;

    // Getters et setters
    public Integer getIdPenalite() {
        return idPenalite;
    }

    public void setIdPenalite(Integer idPenalite) {
        this.idPenalite = idPenalite;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDate dateAction) {
        this.dateAction = dateAction;
    }

    public int getNbrJour() {
        return nbrJour;
    }

    public void setNbrJour(int nbrJour) {
        this.nbrJour = nbrJour;
    }
}
