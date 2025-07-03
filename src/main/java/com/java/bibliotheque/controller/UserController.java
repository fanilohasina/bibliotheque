package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.UserService;

import jakarta.servlet.http.HttpSession;

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
    public String getAll(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("users", userService.getAll());
        return "users/list";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        var userEntity = userService.getById(id);
        if (userEntity.isPresent()) {
            model.addAttribute("user", userEntity.get());
            return "users/detail";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        model.addAttribute("user", new User());
        model.addAttribute("adherents", adherentService.getAll());
        return "users/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (currentUser.getAdherent() == null || !"Admin".equalsIgnoreCase(currentUser.getAdherent().getNom())) {
            return "error/403";
        }
        userService.create(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        var userEntity = userService.getById(id);
        if (userEntity.isPresent()) {
            model.addAttribute("user", userEntity.get());
            model.addAttribute("adherents", adherentService.getAll());
            return "users/edit";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id, @ModelAttribute User user, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }
        if (currentUser.getAdherent() == null || !"Admin".equalsIgnoreCase(currentUser.getAdherent().getNom())) {
            return "error/403";
        }
        userService.update(id, user);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }
        userService.delete(id);
        return "redirect:/users";
    }
}
