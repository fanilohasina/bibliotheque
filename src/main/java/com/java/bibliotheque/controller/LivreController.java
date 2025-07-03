package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.AuteurService;
import com.java.bibliotheque.service.CategorieService;
import com.java.bibliotheque.service.LangueService;
import com.java.bibliotheque.service.LivreService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livres")
public class LivreController {

    private final LivreService livreService;
    private final AuteurService auteurService;
    private final LangueService langueService;
    private final CategorieService categorieService;

    public LivreController(LivreService livreService,
            AuteurService auteurService,
            LangueService langueService,
            CategorieService categorieService) {
        this.livreService = livreService;
        this.auteurService = auteurService;
        this.langueService = langueService;
        this.categorieService = categorieService;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("livres", livreService.getAll());
        return "livres/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        return livreService.getById(id)
                .map(livre -> {
                    model.addAttribute("livre", livre);
                    return "livres/detail";
                })
                .orElse("redirect:/livres");
    }

    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("livre", new Livre());
        model.addAttribute("auteurs", auteurService.getAll());
        model.addAttribute("langues", langueService.getAll());
        model.addAttribute("categories", categorieService.getAll());
        return "livres/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Livre livre, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        livreService.create(livre);
        return "redirect:/livres";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        return livreService.getById(id)
                .map(livre -> {
                    model.addAttribute("livre", livre);
                    model.addAttribute("auteurs", auteurService.getAll());
                    model.addAttribute("langues", langueService.getAll());
                    model.addAttribute("categories", categorieService.getAll());
                    return "livres/edit";
                })
                .orElse("redirect:/livres");
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Livre livre, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        livreService.update(id, livre);
        return "redirect:/livres";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        livreService.delete(id);
        return "redirect:/livres";
    }
}
