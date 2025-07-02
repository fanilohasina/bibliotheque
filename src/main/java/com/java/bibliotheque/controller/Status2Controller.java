package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Status2;
import com.java.bibliotheque.service.Status2Service;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/status2")
public class Status2Controller {

    private final Status2Service service;

    public Status2Controller(Status2Service service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        List<Status2> list = service.getAll();
        model.addAttribute("status2List", list); // Ici le nom doit être "status2List"
        return "status2/list"; // ou ton chemin vers le template
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("status2", new Status2());
        return "status2/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Status2 status2) {
        service.save(status2);
        return "redirect:/status2";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("status2", service.getById(id).orElseThrow());
        return "status2/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Status2 status2) {
        status2.setId_status2(id);
        service.save(status2);
        return "redirect:/status2";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/status2";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("status2", service.getById(id).orElseThrow());
        return "status2/detail";
    }
}