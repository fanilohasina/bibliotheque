package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.Status2;
import com.java.bibliotheque.entite.StatusReservation;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.ReservationService;
import com.java.bibliotheque.service.Status2Service;
import com.java.bibliotheque.service.StatusReservationService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statusReservations")
public class StatusReservationController {

    private final StatusReservationService statusReservationService;
    private final ReservationService reservationService;
    private final Status2Service status2Service;

    public StatusReservationController(StatusReservationService statusReservationService,
            ReservationService reservationService,
            Status2Service status2Service) {
        this.statusReservationService = statusReservationService;
        this.reservationService = reservationService;
        this.status2Service = status2Service;
    }

    // Liste de tous les statusReservations
    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        model.addAttribute("statusReservations", statusReservationService.findAll());
        return "statusReservations/list";
    }

    // Formulaire création
    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        model.addAttribute("statusReservation", new StatusReservation());

        List<Reservation> reservations = reservationService.findAll();
        List<Status2> statuses = status2Service.getAll();

        model.addAttribute("reservations", reservations);
        model.addAttribute("statuses", statuses);

        return "statusReservations/create";
    }

    // Création POST
    @PostMapping("/create")
    public String create(@ModelAttribute StatusReservation statusReservation, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        statusReservationService.save(statusReservation);
        return "redirect:/statusReservations";
    }

    // Formulaire édition
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        StatusReservation statusReservation = statusReservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StatusReservation invalide: " + id));
        model.addAttribute("statusReservation", statusReservation);

        List<Reservation> reservations = reservationService.findAll();
        List<Status2> statuses = status2Service.getAll();

        model.addAttribute("reservations", reservations);
        model.addAttribute("statuses", statuses);

        return "statusReservations/edit";
    }

    // Edition POST
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute StatusReservation statusReservation, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        statusReservation.setId_status_pret(id);
        statusReservationService.save(statusReservation);
        return "redirect:/statusReservations";
    }

    // Détails
    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        StatusReservation statusReservation = statusReservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StatusReservation invalide: " + id));
        model.addAttribute("statusReservation", statusReservation);
        return "statusReservations/details";
    }

    // Suppression
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        statusReservationService.deleteById(id);
        return "redirect:/statusReservations";
    }
}
