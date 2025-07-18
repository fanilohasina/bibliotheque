package com.java.bibliotheque.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.bibliotheque.entite.Exemplaire;
import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.Status2;
import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.entite.StatusReservation;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.LivreRepository;
import com.java.bibliotheque.repository.Status2Repository;
import com.java.bibliotheque.repository.StatusReservationRepository;
import com.java.bibliotheque.service.ExemplaireService;
import com.java.bibliotheque.service.PenaliteService;
import com.java.bibliotheque.service.PretService;
import com.java.bibliotheque.service.ReservationService;
import com.java.bibliotheque.service.Status1Service;
import com.java.bibliotheque.service.StatusPretService;
import com.java.bibliotheque.service.StatusReservationService;
import com.java.bibliotheque.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private PretService pretService;

    @Autowired
    private StatusPretService statusPretService;

    @Autowired
    private StatusReservationService statusReservationService;

    @Autowired
    private ExemplaireService exemplaireService;

    @Autowired
    private Status1Service status1Service;

    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private Status2Repository status2Repository;

    @Autowired
    private StatusReservationRepository statusReservationRepository;

    @GetMapping
    public String home() {
        return "home";
    }
    @GetMapping("home")
    public String home2() {
        return "home1";
    }

    @GetMapping("prets/ajouter")
    public String afficherFormulairePret(@RequestParam(required = false) Integer idLivre,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.getAllExceptAdmin());
        model.addAttribute("livres", livreRepository.findAll());
        model.addAttribute("pret", new Pret());
        model.addAttribute("selectedLivreId", idLivre);

        return "admin/prets/formulaire";
    }

    @PostMapping("prets/ajouter")
    public String enregistrerPret(@ModelAttribute Pret pret, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        System.out.println("Pret: " + pret);
        System.out.println("User: " + pret.getUser());
        System.out.println("Livre: " + pret.getLivre());
        System.out.println("Nbr: " + pret.getNbr());
        System.out.println("Date: " + pret.getDatePret());

        // userService.getById(pret.getUser().getId()).ifPresent(pret::setUser);

        try {
            pretService.ajouterPret(pret);
            model.addAttribute("message", "Prêt enregistré avec succès !");
            model.addAttribute("success", true);
        } catch (Exception e) {
            model.addAttribute("message", "Erreur : " + e.getMessage());
            model.addAttribute("success", false);
        }

        return "admin/prets/message";
    }

    @GetMapping("prets/list")
    public String voirTousLesPrets(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) String etudiant,
            Model model) {

        List<Pret> prets = pretService.getAllPretsParStatutEtudiant(statut, etudiant);
        Map<Integer, StatusPret> derniersStatuts = statusPretService.getDerniersStatutsPourPrets(prets);

        model.addAttribute("prets", prets);
        model.addAttribute("statutsCourants", derniersStatuts); // map : pretId -> StatusPret
        model.addAttribute("statutSelectionne", statut);
        model.addAttribute("etudiantSelectionne", etudiant);
        model.addAttribute("statutsDisponibles", status1Service.getAll());

        return "admin/prets/list";
    }

    @PostMapping("prets/modifier-statut")
    public String modifierStatutPret(
            @RequestParam Long idPret,
            @RequestParam String nouveauStatut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateChangement) {

        pretService.modifierStatut(Integer.parseInt(idPret + ""), nouveauStatut, dateChangement);
        Pret pret = pretService.getById(idPret.intValue()).orElseThrow(() -> new RuntimeException("Prêt non trouvé"));
        pretService.verifierEtAppliquerPenaliteLorsRetour(pret);

        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setLivre(pret.getLivre());
        exemplaire.setDateAction(dateChangement);
        exemplaire.setAction(true);
        exemplaire.setNbr(pret.getNbr());
        exemplaireService.save(exemplaire);

        return "redirect:/admin/prets/list";
    }

    @GetMapping("penalites/list")
    public String voirToutesLesPenalites(
            @RequestParam(required = false) String etudiant,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateRecherche,
            Model model) {

        List<Penalite> penalites = penaliteService.getPenalitesParRecherche(etudiant, dateRecherche);

        model.addAttribute("penalites", penalites);
        model.addAttribute("etudiantSelectionne", etudiant);
        model.addAttribute("dateRechercheSelectionnee", dateRecherche);

        return "admin/penalites/list";
    }

    @GetMapping("reservations/list")
    public String listReservations(@RequestParam(required = false) String nom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        List<Reservation> reservations;

        if ((nom != null && !nom.isEmpty()) || date != null) {
            reservations = reservationService.findByNomOrDate(nom, date);
        } else {
            reservations = reservationService.findAll();
        }

        Map<Integer, StatusReservation> derniersStatuts = statusReservationService
                .getDerniersStatutsParIdPourReservations(reservations);

        model.addAttribute("pret", new Pret());
        model.addAttribute("statutsCourants", derniersStatuts); // map : pretId -> StatusPret
        model.addAttribute("reservations", reservations);
        model.addAttribute("nomRecherche", nom);
        model.addAttribute("dateRecherche", date);

        return "admin/reservations/list";
    }

    @PostMapping("reservation/check")
    public String checkReservation(@RequestParam Long reservationId, Model model) {
        System.out.println(reservationId);
        Reservation reservation = reservationService.findById(reservationId.intValue()).get();
        Pret pret = new Pret();
        pret.setDatePret(reservation.getDateReservation());
        pret.setIsSurPlace(reservation.getIsSurPlace());
        pret.setLivre(reservation.getLivre());
        pret.setNbr(reservation.getNbr());
        pret.setUser(reservation.getUser());

        try {
            pretService.ajouterPret(pret);
            model.addAttribute("message", "Prêt enregistré avec succès !");
            model.addAttribute("success", true);
            Status2 status2 = status2Repository.findByNom("Confirmer");
            StatusReservation statusReservation = new StatusReservation();
            statusReservation.setReservation(reservation);
            statusReservation.setDateAction(reservation.getDateReservation());
            statusReservation.setStatus2(status2);
            statusReservationRepository.save(statusReservation);
        } catch (Exception e) {
            Status2 status2 = status2Repository.findByNom("Annuler");
            StatusReservation statusReservation = new StatusReservation();
            statusReservation.setReservation(reservation);
            statusReservation.setDateAction(reservation.getDateReservation());
            statusReservation.setStatus2(status2);
            statusReservationRepository.save(statusReservation);

            model.addAttribute("message", "Erreur : " + e.getMessage());
            model.addAttribute("success", false);
        }

        return "admin/prets/message";

    }

}
