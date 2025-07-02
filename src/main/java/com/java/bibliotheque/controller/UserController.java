package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.UserService;
import com.java.bibliotheque.service.AdherentService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AdherentService adherentService;

    public UserController(UserService userService, AdherentService adherentService) {
        this.userService = userService;
        this.adherentService = adherentService;
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("users", userService.getAll());
        return "users/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model) {
        var user = userService.getById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "users/detail";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("adherents", adherentService.getAll());
        return "users/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute User user) {
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var user = userService.getById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            model.addAttribute("adherents", adherentService.getAll());
            return "users/edit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute User user) {
        userService.update(id, user);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
