package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.QuotaService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quotas")
public class QuotaController {

    private final QuotaService service;

    public QuotaController(QuotaService service) {
        this.service = service;
    }

    // Liste de tous les quotas
    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("quotas", service.getAll());
        return "quotas/list";
    }

    // Formulaire création quota
    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("quota", new Quota());
        return "quotas/create";
    }

    // Sauvegarde quota (création)
    @PostMapping("/create")
    public String create(@ModelAttribute Quota quota, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.save(quota);
        return "redirect:/quotas";
    }

    // Formulaire édition quota
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Quota quota = service.getById(id).orElseThrow(() -> new IllegalArgumentException("Quota introuvable : " + id));
        model.addAttribute("quota", quota);
        return "quotas/edit";
    }

    // Sauvegarde quota (édition)
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute Quota quota, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        quota.setId_quota(id);
        service.save(quota);
        return "redirect:/quotas";
    }

    // Suppression quota
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        service.delete(id);
        return "redirect:/quotas";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        Quota quota = service.getById(id).orElseThrow(() -> new RuntimeException("Quota non trouvé"));
        model.addAttribute("quota", quota);
        return "quotas/detail";
    }

}
