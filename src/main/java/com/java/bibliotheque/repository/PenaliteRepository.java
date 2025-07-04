package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Penalite;
import com.java.bibliotheque.entite.User;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PenaliteRepository extends JpaRepository<Penalite, Integer> {
        List<Penalite> findByUser(User user);

        @Query(value = "SELECT * FROM penalite p " +
                        "WHERE p.id_user = :userId " +
                        "AND p.date_action <= :datePret " +
                        "AND DATE_ADD(p.date_action, INTERVAL p.nbr_jour DAY) >= :datePret", nativeQuery = true)
        List<Penalite> findPenaliteEnCoursByUser(@Param("userId") Integer userId,
                        @Param("datePret") LocalDate datePret);

        List<Penalite> findByUserNomContainingIgnoreCaseAndDateActionBetween(String nom, LocalDate dateDebut,
                        LocalDate dateFin);

        List<Penalite> findByUserNomContainingIgnoreCase(String nom);

        @Query(value = """
                            SELECT * FROM penalite p
                            WHERE p.date_action <= :dateRecherche
                              AND DATE_ADD(p.date_action, INTERVAL p.nbr_jour DAY) >= :dateRecherche
                        """, nativeQuery = true)
        List<Penalite> findByDateRecherche(@Param("dateRecherche") LocalDate dateRecherche);

        @Query(value = """
                            SELECT * FROM penalite p
                            JOIN user u ON p.user_id = u.id
                            WHERE LOWER(u.nom) LIKE LOWER(CONCAT('%', :etudiant, '%'))
                              AND p.date_action <= :dateRecherche
                              AND DATE_ADD(p.date_action, INTERVAL p.nbr_jour DAY) >= :dateRecherche
                        """, nativeQuery = true)
        List<Penalite> findByEtudiantAndDateRecherche(@Param("etudiant") String etudiant,
                        @Param("dateRecherche") LocalDate dateRecherche);

}
