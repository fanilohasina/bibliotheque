package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.StatusPretService;

import jakarta.servlet.http.HttpSession;

import com.java.bibliotheque.service.PretService;
import com.java.bibliotheque.service.Status1Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/statusprets")
public class StatusPretController {

    private final StatusPretService statusPretService;
    private final PretService pretService; // pour listes prêts (pour formulaire)
    private final Status1Service status1Service; // pour listes status1 (pour formulaire)

    public StatusPretController(StatusPretService statusPretService, PretService pretService,
            Status1Service status1Service) {
        this.statusPretService = statusPretService;
        this.pretService = pretService;
        this.status1Service = status1Service;
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
        model.addAttribute("statusprets", statusPretService.findAll());
        return "statusprets/list"; // Thymeleaf template à créer
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
        model.addAttribute("statusPret", new StatusPret());
        model.addAttribute("prets", pretService.getAll());
        model.addAttribute("status1s", status1Service.getAll());
        return "statusprets/create"; // Formulaire création
    }

    @PostMapping("/create")
    public String create(@ModelAttribute StatusPret statusPret, @RequestParam Integer idPret,
            @RequestParam Long idStatus1, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        statusPret.setPret(pretService.getById(idPret).orElse(null));
        statusPret.setStatus1(status1Service.getById(idStatus1).orElse(null));
        statusPretService.save(statusPret);
        return "redirect:/statusprets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        StatusPret sp = statusPretService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StatusPret introuvable"));
        model.addAttribute("statusPret", sp);
        model.addAttribute("prets", pretService.getAll());
        model.addAttribute("status1s", status1Service.getAll());
        return "statusprets/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute StatusPret statusPret, @RequestParam Integer idPret,
            @RequestParam Long idStatus1, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        statusPret.setPret(pretService.getById(idPret).orElse(null));
        statusPret.setStatus1(status1Service.getById(idStatus1).orElse(null));
        statusPretService.save(statusPret);
        return "redirect:/statusprets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        statusPretService.deleteById(id);
        return "redirect:/statusprets";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        StatusPret sp = statusPretService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("StatusPret introuvable"));
        model.addAttribute("statusPret", sp);
        return "statusprets/detail";
    }
}
