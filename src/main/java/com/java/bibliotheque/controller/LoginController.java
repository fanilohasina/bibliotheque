package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // utilisé pour le formulaire
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User userForm, Model model, HttpSession session) {
        String nom = userForm.getNom();
        String password = userForm.getPassword();

        User user = userService.authenticate(nom, password);

        if (user != null) {
            session.setAttribute("principale", user);
            return "redirect:/membre";
        } else {
            model.addAttribute("error", "Nom ou mot de passe incorrect");
            return "login";
        }
    }

}
