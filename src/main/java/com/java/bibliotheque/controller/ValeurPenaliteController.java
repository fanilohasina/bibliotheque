package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.ValeurPenalite;
import com.java.bibliotheque.service.ValeurPenaliteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/valeur-penalite")
public class ValeurPenaliteController {

    private final ValeurPenaliteService service;

    public ValeurPenaliteController(ValeurPenaliteService service) {
        this.service = service;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("valeurs", service.getAll());
        return "valeur-penalite/list";
    }

    @GetMapping("/create")
    public String formAjout(Model model) {
        model.addAttribute("valeurPenalite", new ValeurPenalite());
        return "valeur-penalite/create";
    }

    @PostMapping("/create")
    public String ajouter(@ModelAttribute ValeurPenalite valeurPenalite) {
        service.save(valeurPenalite);
        return "redirect:/valeur-penalite";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/valeur-penalite";
    }

    @GetMapping("/modifier/{id}")
    public String formModifier(@PathVariable Integer id, Model model) {
        ValeurPenalite vp = service.getById(id).orElseThrow(() -> new RuntimeException("Introuvable"));
        model.addAttribute("valeurPenalite", vp);
        return "valeur-penalite/form";
    }

    @PostMapping("/modifier")
    public String modifier(@ModelAttribute ValeurPenalite valeurPenalite) {
        service.save(valeurPenalite);
        return "redirect:/valeur-penalite";
    }
}
