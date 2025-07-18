package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.entite.Reservation;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.service.LivreService;
import com.java.bibliotheque.service.ReservationService;
import com.java.bibliotheque.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final LivreService livreService;

    public ReservationController(ReservationService reservationService,
            UserService userService,
            LivreService livreService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.livreService = livreService;
    }

    @GetMapping
    public String listReservations(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        List<Reservation> reservations = reservationService.findAll();
        model.addAttribute("reservations", reservations);
        return "reservations/list";
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

        model.addAttribute("reservation", new Reservation());
        model.addAttribute("users", userService.getAll());
        model.addAttribute("livres", livreService.getAll());
        return "reservations/create";
    }

    @PostMapping("/create")
    public String create(@RequestParam Long userId,
            @RequestParam Long livreId,
            @ModelAttribute Reservation reservation,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        User reservationUser = userService.getById(userId).orElseThrow(() -> new IllegalArgumentException("User invalide"));
        Livre livre = livreService.getById(livreId).orElseThrow(() -> new IllegalArgumentException("Livre invalide"));

        reservation.setUser(reservationUser);
        reservation.setLivre(livre);

        reservationService.save(reservation);

        return "redirect:/reservations";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));
        model.addAttribute("reservation", reservation);
        model.addAttribute("users", userService.getAll());
        model.addAttribute("livres", livreService.getAll());
        return "reservations/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Integer id,
            @RequestParam Long userId,
            @RequestParam Long livreId,
            @ModelAttribute Reservation reservation,
            HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        Reservation existing = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation introuvable"));

        User reservationUser = userService.getById(userId).orElseThrow(() -> new IllegalArgumentException("User invalide"));
        Livre livre = livreService.getById(livreId).orElseThrow(() -> new IllegalArgumentException("Livre invalide"));

        existing.setUser(reservationUser);
        existing.setLivre(livre);
        existing.setNbr(reservation.getNbr());
        existing.setIsSurPlace(reservation.getIsSurPlace());

        reservationService.save(existing);

        return "redirect:/reservations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        reservationService.deleteById(id);
        return "redirect:/reservations";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if (user.getAdherent() == null || !"Admin".equalsIgnoreCase(user.getAdherent().getNom())) {
            return "error/403";
        }

        Reservation reservation = reservationService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Réservation invalide Id:" + id));
        model.addAttribute("reservation", reservation);
        return "reservations/detail";
    }
}
