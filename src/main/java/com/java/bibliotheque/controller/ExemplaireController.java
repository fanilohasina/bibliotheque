package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Exemplaire;
import com.java.bibliotheque.service.ExemplaireService;
import com.java.bibliotheque.service.LivreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exemplaires")
public class ExemplaireController {

    private final ExemplaireService exemplaireService;
    private final LivreService livreService;

    public ExemplaireController(ExemplaireService exemplaireService, LivreService livreService) {
        this.exemplaireService = exemplaireService;
        this.livreService = livreService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("exemplaires", exemplaireService.getAll());
        return "exemplaires/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("exemplaire", new Exemplaire());
        model.addAttribute("livres", livreService.getAll());
        return "exemplaires/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Exemplaire exemplaire) {
        exemplaireService.save(exemplaire);
        return "redirect:/exemplaires";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Exemplaire exemplaire = exemplaireService.getById(id).orElseThrow();
        model.addAttribute("exemplaire", exemplaire);
        model.addAttribute("livres", livreService.getAll());
        return "exemplaires/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Exemplaire exemplaire) {
        exemplaire.setId_exemplaire(id);
        exemplaireService.save(exemplaire);
        return "redirect:/exemplaires";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        exemplaireService.delete(id);
        return "redirect:/exemplaires";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Exemplaire exemplaire = exemplaireService.getById(id).orElseThrow();
        model.addAttribute("exemplaire", exemplaire);
        return "exemplaires/detail";
    }
}
