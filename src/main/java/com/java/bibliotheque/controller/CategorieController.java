package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Categorie;
import com.java.bibliotheque.service.CategorieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("categories", service.getAll());
        return "categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("categorie", new Categorie());
        return "categories/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Categorie categorie) {
        service.create(categorie);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Categorie> cat = service.getById(id);
        if (cat.isPresent()) {
            model.addAttribute("categorie", cat.get());
            return "categories/edit";
        }
        return "redirect:/categories";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Categorie categorie) {
        service.update(id, categorie);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/categories";
    }
}
