package com.java.bibliotheque.entite;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "status_reservation")
public class StatusReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStatusReservation;

    public Integer getIdStatusReservation() {
        return idStatusReservation;
    }

    public void setIdStatusReservation(Integer idStatusReservation) {
        this.idStatusReservation = idStatusReservation;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status2", nullable = false)
    private Status2 status2;

    private LocalDate dateAction;

    // Getters & Setters

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Status2 getStatus2() {
        return status2;
    }

    public void setStatus2(Status2 status2) {
        this.status2 = status2;
    }

    public LocalDate getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDate dateAction) {
        this.dateAction = dateAction;
    }
}
