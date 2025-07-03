package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Auteur;
import com.java.bibliotheque.service.AuteurService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.java.bibliotheque.entite.User;

@Controller
@RequestMapping("/auteurs")
public class AuteurController {

    private final AuteurService service;

    public AuteurController(AuteurService service) {
        this.service = service;
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("auteurs", service.getAll());
        return "auteurs/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        var auteur = service.getById(id);
        if (auteur.isPresent()) {
            model.addAttribute("auteur", auteur.get());
            return "auteurs/detail";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("auteur", new Auteur());
        return "auteurs/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Auteur auteur, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.create(auteur);
        return "redirect:/auteurs";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        var auteur = service.getById(id);
        if (auteur.isPresent()) {
            model.addAttribute("auteur", auteur.get());
            return "auteurs/edit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Auteur auteur, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.update(id, auteur);
        return "redirect:/auteurs";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.delete(id);
        return "redirect:/auteurs";
    }
}
