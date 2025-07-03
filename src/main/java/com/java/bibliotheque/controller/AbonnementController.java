package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Abonnement;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.AbonnementService;
import com.java.bibliotheque.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/abonnements")
public class AbonnementController {

    private final AbonnementService service;
    private final UserService userService;

    public AbonnementController(AbonnementService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (user.getAdherent() == null ||
                !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // page interdite ou message d’erreur
        }
        model.addAttribute("abonnements", service.getAll());
        return "abonnements/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Vérifie que l’adherent est "Admin"
        if (user.getAdherent() == null ||
                !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // page interdite ou message d’erreur
        }
        model.addAttribute("abonnement", new Abonnement());
        model.addAttribute("users", userService.getAll());
        return "abonnements/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Abonnement abonnement, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Vérifie que l’adherent est "Admin"
        if (user.getAdherent() == null ||
                !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // page interdite ou message d’erreur
        }
        service.save(abonnement);
        return "redirect:/abonnements";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Vérifie que l’adherent est "Admin"
        if (user.getAdherent() == null ||
                !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // page interdite ou message d’erreur
        }
        service.delete(id);
        return "redirect:/abonnements";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Vérifie que l’adherent est "Admin"
        if (user.getAdherent() == null ||
                !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // page interdite ou message d’erreur
        }
        service.getById(id).ifPresentOrElse(
                abonnement -> model.addAttribute("abonnement", abonnement),
                () -> {
                    throw new IllegalArgumentException("Abonnement non trouvé : " + id);
                });
        return "abonnements/detail"; // Template Thymeleaf pour afficher le détail
    }

    // --- EDIT FORM ---
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // Vérifie que l’adherent est "Admin"
        if (user.getAdherent() == null ||
                !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403"; // page interdite ou message d’erreur
        }
        service.getById(id).ifPresentOrElse(
                abonnement -> {
                    model.addAttribute("abonnement", abonnement);
                    model.addAttribute("users", userService.getAll()); // liste des users pour le select
                },
                () -> {
                    throw new IllegalArgumentException("Abonnement non trouvé : " + id);
                });
        return "abonnements/edit"; // Template Thymeleaf pour le formulaire d'édition
    }

    // --- TRAITEMENT EDIT ---
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Abonnement abonnement) {
        // Pour éviter de créer un nouveau, s'assurer que l'id est bien celui du path
        abonnement.setId_abonnement(id);
        service.save(abonnement);
        return "redirect:/abonnements";
    }
}
