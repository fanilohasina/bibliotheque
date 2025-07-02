package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.service.QuotaService;
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
    public String list(Model model) {
        model.addAttribute("quotas", service.getAll());
        return "quotas/list";
    }

    // Formulaire création quota
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("quota", new Quota());
        return "quotas/create";
    }

    // Sauvegarde quota (création)
    @PostMapping("/create")
    public String create(@ModelAttribute Quota quota) {
        service.save(quota);
        return "redirect:/quotas";
    }

    // Formulaire édition quota
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Quota quota = service.getById(id).orElseThrow(() -> new IllegalArgumentException("Quota introuvable : " + id));
        model.addAttribute("quota", quota);
        return "quotas/edit";
    }

    // Sauvegarde quota (édition)
    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute Quota quota) {
        quota.setId_quota(id);
        service.save(quota);
        return "redirect:/quotas";
    }

    // Suppression quota
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.delete(id);
        return "redirect:/quotas";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Quota quota = service.getById(id).orElseThrow(() -> new RuntimeException("Quota non trouvé"));
        model.addAttribute("quota", quota);
        return "quotas/detail";
    }

}
