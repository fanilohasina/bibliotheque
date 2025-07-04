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

import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.LivreRepository;
import com.java.bibliotheque.service.PenaliteService;
import com.java.bibliotheque.service.PretService;
import com.java.bibliotheque.service.Status1Service;
import com.java.bibliotheque.service.StatusPretService;
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
    private Status1Service status1Service;

    @Autowired
    private PenaliteService penaliteService;

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
        userService.getById(pret.getUser().getId()).ifPresent(pret::setUser);

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

}
