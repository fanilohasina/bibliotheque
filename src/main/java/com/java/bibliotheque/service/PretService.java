package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Abonnement;
import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.entite.Status1;
import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.AbonnementRepository;
import com.java.bibliotheque.repository.ExemplaireRepository;
import com.java.bibliotheque.repository.PretRepository;
import com.java.bibliotheque.repository.Status1Repository;
import com.java.bibliotheque.repository.StatusPretRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PretService {
    @Autowired
    private PretRepository pretRepository;

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private ExemplaireRepository exemplaireRepository;

    @Autowired
    private Status1Repository status1Repository;

    @Autowired
    private StatusPretRepository statusPretRepository;

    private final PretRepository repository;

    public PretService(PretRepository repository) {
        this.repository = repository;
    }

    public List<Pret> getAll() {
        return repository.findAll();
    }

    public Optional<Pret> getById(Integer id) {
        return repository.findById(id);
    }

    public Pret save(Pret pret) {
        return repository.save(pret);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<Pret> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Pret> findByUserAndLivreTitre(User user, String titre) {
        return repository.findByUserAndLivre_TitreContainingIgnoreCase(user, titre);
    }

    public void ajouterPret(Pret pret) throws Exception {
        User user = pret.getUser();
        Livre livre = pret.getLivre();
        int nbr = pret.getNbr();
        boolean isSurPlace = pret.getIsSurPlace();
        LocalDate datePret = pret.getDatePret();

        // Vérifie si un abonnement est actif à cette date
        List<Abonnement> abonnements = abonnementRepository.findAbonnementActif(user.getId(), datePret);
        if (abonnements.isEmpty()) {
            throw new Exception("Aucun abonnement actif pour cet utilisateur à la date du prêt.");
        }

        // Vérifie si un quota est défini pour l'adhérent
        Quota quota = user.getAdherent().getQuota();
        if (quota == null) {
            throw new Exception("Aucun quota défini pour l'adhérent.");
        }

        // Vérifie le quota de prêts
        int totalPrets = pretRepository.countPretsActifs(user.getId());
        if (!isSurPlace && totalPrets + nbr > quota.getNbr_livre_max_pret()) {
            throw new Exception("Quota de prêts dépassé.");
        }

        // Vérifie le stock disponible
        int stockDisponible = exemplaireRepository.totalStockDisponible(livre.getId());
        if (stockDisponible < nbr) {
            throw new Exception("Pas assez d'exemplaires disponibles.");
        }

        // Enregistre le prêt
        pret.setDatePret(datePret);
        pretRepository.save(pret);

        // Ajoute le statut initial du prêt
        StatusPret statusPret = new StatusPret();
        statusPret.setPret(pret);
        statusPret.setDateAction(datePret);

        // Tu peux remplacer 1 par l'ID réel du statut initial (ex: "En cours")
        Status1 statutInitial = status1Repository.findByNom("En cours");
        if (statutInitial == null) {
            throw new Exception("Statut initial 'En cours' non trouvé.");
        }

        statusPret.setStatus1(statutInitial);
        statusPretRepository.save(statusPret);
    }

}
