package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.*;
import com.java.bibliotheque.repository.ExemplaireRepository;
import com.java.bibliotheque.service.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
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
    private final PenaliteService penaliteService;

    public MembreController(
            LivreService livreService,
            PretService pretService,
            ReservationService reservationService, ExemplaireRepository exemplaireRepository,
            PenaliteService penaliteService) {
        this.exemplaireRepository = exemplaireRepository;
        this.livreService = livreService;
        this.pretService = pretService;
        this.reservationService = reservationService;
        this.penaliteService = penaliteService;
    }

    @GetMapping("/penalites")
    public String listePenalites(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        List<Penalite> penalites = penaliteService.getPenalitesByUser(user);
        model.addAttribute("penalites", penalites);
        return "membre/penalites";
    }

    @GetMapping
    public String listeLivres(@RequestParam(required = false) String searchType,
            @RequestParam(required = false) String search,
            Model model,
            HttpSession session) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        if (user.getAdherent() != null && "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        List<Livre> livres;

        if (search != null && !search.isEmpty()) {
            switch (searchType) {
                case "auteur":
                    livres = livreService.searchByAuteur(search);
                    break;
                case "categorie":
                    livres = livreService.searchByCategorie(search);
                    break;
                default:
                    livres = livreService.searchByTitre(search);
                    break;
            }
        } else {
            livres = livreService.getAll();
        }

        // Exemple: Map ID livre -> nb exemplaires restants
        Map<Long, Integer> exemplaireCounts = new HashMap<>();
        for (Object[] row : exemplaireRepository.getExemplaireCountPerLivre()) {
            Long livreId = (Long) row[0];
            Integer total = ((Number) row[1]).intValue();
            exemplaireCounts.put(livreId, total);
        }

        model.addAttribute("livres", livres);
        model.addAttribute("exemplaireCounts", exemplaireCounts);
        model.addAttribute("searchType", searchType);
        model.addAttribute("search", search);

        return "membre/livres";
    }

    @GetMapping("/prets")
    public String listePrets(Model model, HttpSession session, @RequestParam(required = false) String livre) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Pas connecté
        }

        if (user.getAdherent() != null &&
                "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // Admin interdit ici
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
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Pas connecté
        }

        if (user.getAdherent() != null &&
                "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // Admin interdit ici
        }

        List<Reservation> reservations = reservationService.findByUser(user);
        model.addAttribute("reservations", reservations);
        return "membre/reservations";
    }

    // Réserver un livre
    @GetMapping("/livres/reserver")
    public String reserverForm(@RequestParam Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Pas connecté
        }

        if (user.getAdherent() != null &&
                "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // Admin interdit ici
        }
        Livre livre = livreService.getById(id).orElseThrow();
        model.addAttribute("livre", livre);
        model.addAttribute("reservation", new Reservation());
        return "membre/reserver"; // template Thymeleaf attendu
    }

    @PostMapping("/livres/reserver")
    public String reserver(@RequestParam("id") Long id, @ModelAttribute Reservation reservation, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login"; // Pas connecté
        }

        if (user.getAdherent() != null &&
                "Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // Admin interdit ici
        }

        Livre livre = livreService.getById(id).orElseThrow();

        reservation.setUser(user);
        reservation.setLivre(livre);
        reservationService.save(reservation);

        return "redirect:/membre/reservations";
    }

    @PostMapping("/prets/prolonger")
    public String prolongerPret(
            @RequestParam Integer idPret,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDemande,
            @RequestParam int joursVoulu,
            RedirectAttributes redirectAttributes) {

        try {
            pretService.prolongerPret(idPret, dateDemande, joursVoulu);
            redirectAttributes.addFlashAttribute("success", "Prolongement effectué avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/membre/prets";
    }

}
