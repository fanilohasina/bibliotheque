package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.repository.ExemplaireRepository;
import com.java.bibliotheque.service.LivreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin") // <-- Retourne des données JSON
public class ApiAdminController {

    private final LivreService livreService;
    private final ExemplaireRepository exemplaireRepository;

    @Autowired
    public ApiAdminController(LivreService livreService, ExemplaireRepository exemplaireRepository) {
        this.livreService = livreService;
        this.exemplaireRepository = exemplaireRepository;
    }

    // GET /api/admin/livres
    @GetMapping("/livres")
    public List<Livre> getAllStatusReservations() {
        return livreService.getAll();
    }

    // @GetMapping("/livres/{id}")
    // public Livre getAllStatusReservations(@PathVariable Long id,
    // @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date)
    // {
    // Map<Long, Integer> exemplaireCounts = new HashMap<>();

    // for (Object[] row : exemplaireRepository.getExemplaireCountPerLivre()) {
    // Long livreId = (Long) row[0];
    // Integer total = ((Number) row[1]).intValue();
    // exemplaireCounts.put(livreId, total);
    // }
    // return livreService.getById(id).get();
    // }

    @GetMapping("/livres/{id}")
    public ResponseEntity<Map<String, Object>> getLivreAvecExemplaires(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Optional<Livre> livreOptional = livreService.getById(id);
        if (livreOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Livre livre = livreOptional.get();

        // Compte les exemplaires disponibles à la date donnée
        Integer total = exemplaireRepository.totalStockDisponible(id, date);
        if (total == null)
            total = 0;

        // Sous-objet JSON "livre"
        Map<String, Object> livreMap = new HashMap<>();
        livreMap.put("idLivre", livre.getId());
        livreMap.put("titre", livre.getTitre());
        livreMap.put("auteur", livre.getAuteur());

        // Objet final à retourner
        Map<String, Object> response = new HashMap<>();
        response.put("livre", livreMap);
        response.put("nombreExemplaires", total);

        return ResponseEntity.ok(response);
    }

}
