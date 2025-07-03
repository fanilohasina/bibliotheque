package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Adherent;
import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.AdherentService;
import com.java.bibliotheque.service.QuotaService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adherents")
public class AdherentController {

    private final AdherentService adherentService;
    private final QuotaService quotaService;

    public AdherentController(AdherentService adherentService, QuotaService quotaService) {
        this.adherentService = adherentService;
        this.quotaService = quotaService;
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
        model.addAttribute("adherents", adherentService.getAll());
        return "adherents/list";
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
        model.addAttribute("adherent", new Adherent());
        model.addAttribute("quotas", quotaService.getAll());
        return "adherents/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam Integer quotaId, @ModelAttribute Adherent adherent, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Quota quota = quotaService.getById(quotaId)
                .orElseThrow(() -> new IllegalArgumentException("Quota invalide"));
        adherent.setQuota(quota);
        adherentService.save(adherent);
        return "redirect:/adherents";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Adherent adherent = adherentService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adhérent invalide"));
        List<Quota> quotas = quotaService.getAll();

        model.addAttribute("adherent", adherent);
        model.addAttribute("quotas", quotas);
        return "adherents/edit";
    }

    @PostMapping("/edit")
    public String editSubmit(@RequestParam Integer quotaId, @ModelAttribute Adherent adherent, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Quota quota = quotaService.getById(quotaId)
                .orElseThrow(() -> new IllegalArgumentException("Quota invalide"));
        adherent.setQuota(quota);
        adherentService.save(adherent);
        return "redirect:/adherents";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        adherentService.delete(id);
        return "redirect:/adherents";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || 
            !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Adherent adherent = adherentService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Adherent inconnu : " + id));
        model.addAttribute("adherent", adherent);
        return "adherents/detail";
    }
}
