package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.*;
import com.java.bibliotheque.repository.ExemplaireRepository;
import com.java.bibliotheque.service.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/membre")
public class MembreController {

    private final LivreService livreService;
    private final PretService pretService;
    private final ReservationService reservationService;
    private final ExemplaireRepository exemplaireRepository;

    public MembreController(
            LivreService livreService,
            PretService pretService,
            ReservationService reservationService, ExemplaireRepository exemplaireRepository) {
        this.exemplaireRepository = exemplaireRepository;
        this.livreService = livreService;
        this.pretService = pretService;
        this.reservationService = reservationService;
    }

    // @GetMapping
    // public String menu(HttpSession session) {
    // User user = (User) session.getAttribute("user");
    // if (user == null) {
    // return "redirect:/login"; // Pas connecté
    // }

    // if (user.getAdherent() != null &&
    // "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
    // return "error/403"; // Admin interdit ici
    // }
    // return "membre/menu";
    // }

    @GetMapping
    public String listeLivres(@RequestParam(required = false) String titre, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (user.getAdherent() != null && "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        List<Livre> livres = (titre != null && !titre.isEmpty())
                ? livreService.searchByTitre(titre)
                : livreService.getAll();

        // Récupérer le nombre d'exemplaires par livre
        List<Object[]> exemplaireCounts = exemplaireRepository.getExemplaireCountPerLivre();
        Map<Long, Integer> livreExemplaireMap = new HashMap<>();
        for (Object[] row : exemplaireCounts) {
            Long livreId = (Long) row[0];
            Integer total = ((Number) row[1]).intValue();
            livreExemplaireMap.put(livreId, total);
        }

        model.addAttribute("livres", livres);
        model.addAttribute("titreRecherche", titre);
        model.addAttribute("exemplaireCounts", livreExemplaireMap);

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
