package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "status_pret")
public class StatusPret {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_status_pret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pret", nullable = false)
    private Pret pret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status1", nullable = false)
    private Status1 status1;

    private LocalDate dateAction;

    // Getters et setters

    public Integer getId_status_pret() {
        return id_status_pret;
    }

    public void setId_status_pret(Integer id_status_pret) {
        this.id_status_pret = id_status_pret;
    }

    public Pret getPret() {
        return pret;
    }

    public void setPret(Pret pret) {
        this.pret = pret;
    }

    public Status1 getStatus1() {
        return status1;
    }

    public void setStatus1(Status1 status1) {
        this.status1 = status1;
    }

    public LocalDate getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDate dateAction) {
        this.dateAction = dateAction;
    }
}
