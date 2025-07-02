package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Langue;
import com.java.bibliotheque.service.LangueService;
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
    public String getAll(Model model) {
        model.addAttribute("langues", service.getAll());
        return "langues/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        var langue = service.getById(id);
        if (langue.isPresent()) {
            model.addAttribute("langue", langue.get());
            return "langues/detail";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("langue", new Langue());
        return "langues/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Langue langue) {
        service.create(langue);
        return "redirect:/langues";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var langue = service.getById(id);
        if (langue.isPresent()) {
            model.addAttribute("langue", langue.get());
            return "langues/edit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Langue langue) {
        service.update(id, langue);
        return "redirect:/langues";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/langues";
    }
}
