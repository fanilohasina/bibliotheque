package com.java.bibliotheque.controller;

import com.java.bibliotheque.entite.Abonnement;
import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.repository.AbonnementRepository;
import com.java.bibliotheque.repository.ExemplaireRepository;
import com.java.bibliotheque.repository.StatusPretRepository;
import com.java.bibliotheque.service.LivreService;
import com.java.bibliotheque.service.PenaliteService;
import com.java.bibliotheque.service.PretService;
import com.java.bibliotheque.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class ApiAdminController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private PenaliteService penaliteService;

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StatusPretRepository statusPretRepository;

    @Autowired
    private PretService pretService;

    // GET /api/admin/livres
    @GetMapping("/livres")
    public List<Livre> getAllStatusReservations() {
        return livreService.getAll();
    }

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
        // if (total == null)
        // total = 0;

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

    @GetMapping("/profil")
    public Map<String, Object> getProfilMembre(
            @RequestParam("id") Long userId,
            @RequestParam("date") String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        User user = userService.getById(userId).get();
        Map<String, Object> response = new HashMap<>();

        // 1. Pénalités
        List<Penalite> penalites = penaliteService.getPenalitesByUser(user);
        List<Map<String, Object>> penaliteList = penalites.stream().map(p -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date_action", p.getDateAction());
            map.put("nbr_jours", p.getNbrJour());
            return map;
        }).collect(Collectors.toList());

        // 2. Quota
        // Quota quota = quotaService.getQuotaByUser(user);
        Quota quota = user.getAdherent().getQuota();
        Map<String, Object> quotaMap = new HashMap<>();
        quotaMap.put("nbr_jours_max_prets", quota.getNbr_jour_max_pret());
        quotaMap.put("nbr_livre_max_prets", quota.getNbr_livre_max_pret());

        // 3. Abonnement
        List<Abonnement> abonnement = abonnementRepository.findAbonnementActif(user.getId(), date);
        Map<String, Object> aboMap = new HashMap<>();
        if (!abonnement.isEmpty()) {
            aboMap.put("date_debut", abonnement.get(0).getDate_debut());
            aboMap.put("date_fin", abonnement.get(0).getDate_fin());
        } else {
            aboMap.put("date_debut", null);
            aboMap.put("date_fin", null);
        }

        List<Pret> prets = pretService.findByUser(user);
        // 4. Prêts en cours (pas encore retournés)
        List<Map<String, Object>> pretList = prets.stream()
                .filter(p -> {
                    List<StatusPret> statuts = statusPretRepository.findByPret(p);
                    // Vérifie s'il y a un statut "retourné" (id == 3) à une date <= date analysée
                    return statuts.stream()
                            .filter(s -> s.getId_status_pret() == 3)
                            .noneMatch(s -> !s.getDateAction().isAfter(date));
                })
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", p.getIdPret());
                    map.put("datePret", p.getDatePret());

                    // Cherche la date de retour s’il existe
                    List<StatusPret> statuts = statusPretRepository.findByPret(p);
                    Optional<LocalDate> retour = statuts.stream()
                            .filter(s -> s.getId_status_pret() == 3)
                            .map(StatusPret::getDateAction)
                            .min(Comparator.naturalOrder()); // la première date de retour

                    map.put("dateRetour", retour.orElse(null));
                    return map;
                }).collect(Collectors.toList());

        response.put("penalites", penaliteList);
        response.put("quota", quotaMap);
        response.put("abonnement", aboMap);
        response.put("prets", pretList);
        return response;
    }

}
