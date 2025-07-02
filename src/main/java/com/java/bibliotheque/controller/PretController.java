package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.service.PretService;
import com.java.bibliotheque.service.UserService;
import com.java.bibliotheque.service.LivreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prets")
public class PretController {

    private final PretService pretService;
    private final UserService userService;
    private final LivreService livreService;

    public PretController(PretService pretService, UserService userService, LivreService livreService) {
        this.pretService = pretService;
        this.userService = userService;
        this.livreService = livreService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("prets", pretService.getAll());
        return "prets/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        Pret pret = new Pret();
        pret.setUser(new User()); // évite le null pour le template
        pret.setLivre(new Livre()); // évite le null pour le template

        model.addAttribute("pret", pret);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("livres", livreService.getAll());
        return "prets/create";
    }

    @PostMapping("/create")
    public String create(
            @RequestParam Long userId,
            @RequestParam Long livreId,
            @ModelAttribute Pret pret) {

        User user = userService.getById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur invalide"));
        Livre livre = livreService.getById(livreId)
                .orElseThrow(() -> new IllegalArgumentException("Livre invalide"));

        pret.setUser(user);
        pret.setLivre(livre);

        pretService.save(pret);

        return "redirect:/prets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        Pret pret = pretService.getById(id).orElseThrow(() -> new IllegalArgumentException("Pret inconnu : " + id));
        model.addAttribute("pret", pret);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("livres", livreService.getAll());
        return "prets/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, @ModelAttribute Pret pret) {
        pret.setId_pret(id);
        pretService.save(pret);
        return "redirect:/prets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        pretService.delete(id);
        return "redirect:/prets";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Pret pret = pretService.getById(id).orElseThrow(() -> new IllegalArgumentException("Pret inconnu : " + id));
        model.addAttribute("pret", pret);
        return "prets/detail";
    }
}
