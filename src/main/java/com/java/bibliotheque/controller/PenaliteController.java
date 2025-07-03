package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.service.PenaliteService;
import com.java.bibliotheque.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/penalites")
public class PenaliteController {

    private final PenaliteService penaliteService;
    private final UserService userService;

    public PenaliteController(PenaliteService penaliteService, UserService userService) {
        this.penaliteService = penaliteService;
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("penalites", penaliteService.findAll());
        return "penalites/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("penalite", new Penalite());
        model.addAttribute("users", userService.getAll());
        return "penalites/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Penalite penalite) {
        penaliteService.save(penalite);
        return "redirect:/penalites";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Penalite penalite = penaliteService.findById(id);
        model.addAttribute("penalite", penalite);
        model.addAttribute("users", userService.getAll());
        return "penalites/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Penalite penalite) {
        penaliteService.save(penalite);
        return "redirect:/penalites";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        penaliteService.deleteById(id);
        return "redirect:/penalites";
    }
}
