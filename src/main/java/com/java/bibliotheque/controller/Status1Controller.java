package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Status1;
import com.java.bibliotheque.service.Status1Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/status1")
public class Status1Controller {

    private final Status1Service service;

    public Status1Controller(Status1Service service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("statusList", service.getAll());
        return "status1/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("status", new Status1());
        return "status1/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Status1 status) {
        service.save(status);
        return "redirect:/status1";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Status1 status = service.getById(id).orElseThrow();
        model.addAttribute("status", status);
        return "status1/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute Status1 status) {
        status.setId_status1(id);
        service.save(status);
        return "redirect:/status1";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/status1";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Status1 status = service.getById(id).orElseThrow();
        model.addAttribute("status", status);
        return "status1/detail";
    }
}
