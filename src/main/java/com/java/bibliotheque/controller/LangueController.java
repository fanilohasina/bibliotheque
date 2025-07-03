package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Langue;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.LangueService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/langues")
public class LangueController {

    private final LangueService service;

    public LangueController(LangueService service) {
        this.service = service;
    }

    @GetMapping
    public String getAll(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("langues", service.getAll());
        return "langues/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        var langue = service.getById(id);
        if (langue.isPresent()) {
            model.addAttribute("langue", langue.get());
            return "langues/detail";
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
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("langue", new Langue());
        return "langues/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Langue langue, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.create(langue);
        return "redirect:/langues";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        var langue = service.getById(id);
        if (langue.isPresent()) {
            model.addAttribute("langue", langue.get());
            return "langues/edit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Langue langue, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.update(id, langue);
        return "redirect:/langues";
    }

    @PostMapping("/delete/{id}")
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
        return "redirect:/langues";
    }
}
