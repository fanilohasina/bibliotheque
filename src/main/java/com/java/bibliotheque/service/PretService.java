package com.java.bibliotheque.service;

import com.java.bibliotheque.entite.Abonnement;
import com.java.bibliotheque.entite.Exemplaire;
import com.java.bibliotheque.entite.Livre;
import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.Quota;
import com.java.bibliotheque.entite.Status1;
import com.java.bibliotheque.entite.StatusPret;
import com.java.bibliotheque.entite.User;
import com.java.bibliotheque.repository.AbonnementRepository;
import com.java.bibliotheque.repository.ExemplaireRepository;
import com.java.bibliotheque.repository.PenaliteRepository;
import com.java.bibliotheque.repository.PretRepository;
import com.java.bibliotheque.repository.Status1Repository;
import com.java.bibliotheque.repository.StatusPretRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

    @Autowired
    private PenaliteRepository penaliteRepository;

    @Autowired
    private ExemplaireService exemplaireService;

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

        // Vérifie si l'utilisateur a des pénalités en cours
        LocalDate aujourdHui = pret.getDatePret();
        List<Penalite> penalitesEnCours = penaliteRepository
                .findPenaliteEnCoursByUser(user.getId().intValue(), aujourdHui);
        if (!penalitesEnCours.isEmpty()) {
            throw new Exception("L'utilisateur a des pénalités en cours, il ne peut pas effectuer de prêt.");
        }

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

        // Récupère le statut initial (ex: "En cours")
        Status1 statutInitial = status1Repository.findByNom("En cours");
        if (statutInitial == null) {
            throw new Exception("Statut initial 'En cours' non trouvé.");
        }

        statusPret.setStatus1(statutInitial);
        statusPretRepository.save(statusPret);

        Exemplaire exemplaire = new Exemplaire();
        exemplaire.setLivre(pret.getLivre());
        exemplaire.setDateAction(aujourdHui);
        exemplaire.setAction(false);
        exemplaire.setNbr(pret.getNbr());
        exemplaireService.save(exemplaire);
    }

    public List<Pret> getAllPretsParStatut(String statut) {
        return pretRepository.findAllPretsByStatut(statut);
    }

    public List<Pret> getAllPretsParStatutEtudiant(String statut, String etudiant) {
        if ((statut == null || statut.isEmpty()) && (etudiant == null || etudiant.isEmpty())) {
            return pretRepository.findAll();
        }
        return pretRepository.findPretsByStatusEtudiant(
                (statut == null || statut.isEmpty()) ? null : statut,
                (etudiant == null || etudiant.isEmpty()) ? null : etudiant);
    }

    public void modifierStatut(Integer idPret, String nouveauStatut, LocalDate dateChangement) {
        Pret pret = this.getById(idPret).orElseThrow();

        Status1 statut = status1Repository.findByNom(nouveauStatut); // ou autre méthode
        StatusPret nouveau = new StatusPret();
        nouveau.setPret(pret);
        nouveau.setStatus1(statut);
        nouveau.setDateAction(dateChangement);

        statusPretRepository.save(nouveau);
    }

    public void verifierEtAppliquerPenaliteLorsRetour(Pret pret) {
        List<StatusPret> statusList = statusPretRepository.findByPretOrderByDateActionAsc(pret);

        LocalDate dateDebut = null;
        LocalDate dateRetour = null;

        for (StatusPret s : statusList) {
            if ("En cours".equalsIgnoreCase(s.getStatus1().getNom())) {
                dateDebut = s.getDateAction();
            } else if ("Retourner".equalsIgnoreCase(s.getStatus1().getNom())) {
                dateRetour = s.getDateAction();
            }
        }

        if (dateDebut != null && dateRetour != null) {
            long joursEffectifs = ChronoUnit.DAYS.between(dateDebut, dateRetour);
            Integer dureeAutorisee = pret.getUser().getAdherent().getQuota().getNbr_jour_max_pret();

            int retard = (int) (joursEffectifs - dureeAutorisee);
            if (retard > 0) {
                User user = pret.getUser();

                // Cherche une pénalité encore en cours pour l’utilisateur
                List<Penalite> penalitesEnCours = penaliteRepository
                        .findPenaliteEnCoursByUser(user.getId().intValue(), dateRetour);

                if (!penalitesEnCours.isEmpty()) {
                    Penalite penaliteExistante = penalitesEnCours.get(0);
                    penaliteExistante.setNbrJour((penaliteExistante.getNbrJour() + retard));
                    penaliteRepository.save(penaliteExistante);
                } else {
                    // Crée une nouvelle pénalité
                    Penalite penalite = new Penalite();
                    penalite.setUser(user);
                    penalite.setDateAction(dateRetour);
                    penalite.setNbrJour(retard);
                    penaliteRepository.save(penalite);
                }
            }
        }
    }

}
