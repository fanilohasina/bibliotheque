package com.java.bibliotheque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.LivreRepository;
import com.java.bibliotheque.service.PretService;
import com.java.bibliotheque.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/prets")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private PretService pretService;

    @GetMapping("/ajouter")
    public String afficherFormulairePret(@RequestParam(required = false) Integer idLivre,
            Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("users", userService.getAll());
        model.addAttribute("livres", livreRepository.findAll());
        model.addAttribute("pret", new Pret());
        model.addAttribute("selectedLivreId", idLivre);

        return "admin/prets/formulaire";
    }

    @PostMapping("/ajouter")
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
}
