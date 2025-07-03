package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Categorie;
import com.java.bibliotheque.service.CategorieService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import com.java.bibliotheque.entite.User;

@Controller
@RequestMapping("/categories")
public class CategorieController {

    private final CategorieService service;

    public CategorieController(CategorieService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("categories", service.getAll());
        return "categories/list";
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("categorie", new Categorie());
        return "categories/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Categorie categorie, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.create(categorie);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Optional<Categorie> cat = service.getById(id);
        if (cat.isPresent()) {
            model.addAttribute("categorie", cat.get());
            return "categories/edit";
        }
        return "redirect:/categories";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Categorie categorie, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.update(id, categorie);
        return "redirect:/categories";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.delete(id);
        return "redirect:/categories";
    }
}
