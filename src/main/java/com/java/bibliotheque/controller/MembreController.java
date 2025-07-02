package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.*;
import com.java.bibliotheque.service.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/membre")
public class MembreController {

    private final LivreService livreService;
    private final PretService pretService;
    private final ReservationService reservationService;
    private final UserService userService;

    public MembreController(
            LivreService livreService,
            PretService pretService,
            ReservationService reservationService,
            UserService userService) {
        this.livreService = livreService;
        this.pretService = pretService;
        this.reservationService = reservationService;
        this.userService = userService;
    }

    @GetMapping
    public String menu() {
        return "membre/menu";
    }

    @GetMapping("/livres")
    public String listeLivres(@RequestParam(required = false) String titre, Model model) {
        List<Livre> livres = (titre != null && !titre.isEmpty())
                ? livreService.searchByTitre(titre)
                : livreService.getAll();
        model.addAttribute("livres", livres);
        model.addAttribute("titreRecherche", titre);
        return "membre/livres";
    }

    @GetMapping("/prets")
    public String listePrets(Model model, HttpSession session, @RequestParam(required = false) String livre) {
        User user = (User) session.getAttribute("principale"); // même clé que dans login
        if (user == null) {
            return "redirect:/login"; // utilisateur non connecté
        }
        List<Pret> prets = (livre != null && !livre.isEmpty())
                ? pretService.findByUserAndLivreTitre(user, livre)
                : pretService.findByUser(user);
        model.addAttribute("prets", prets);
        model.addAttribute("livreRecherche", livre);
        return "membre/prets";
    }

    @GetMapping("/reservations")
    public String listeReservations(Model model, HttpSession session) {
        User user = (User) session.getAttribute("principale");
        if (user == null) {
            return "redirect:/login"; // utilisateur non connecté
        }

        List<Reservation> reservations = reservationService.findByUser(user);
        model.addAttribute("reservations", reservations);
        return "membre/reservations";
    }

    // Réserver un livre
    @GetMapping("/livres/reserver")
    public String reserverForm(@RequestParam Long id, Model model) {
        Livre livre = livreService.getById(id).orElseThrow();
        model.addAttribute("livre", livre);
        model.addAttribute("reservation", new Reservation());
        return "membre/reserver"; // template Thymeleaf attendu
    }

    @PostMapping("/livres/reserver")
    public String reserver(@RequestParam("id") Long id, @ModelAttribute Reservation reservation, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Livre livre = livreService.getById(id).orElseThrow();

        reservation.setUser(user);
        reservation.setLivre(livre);
        reservationService.save(reservation);

        return "redirect:/membre/reservations";
    }

}
