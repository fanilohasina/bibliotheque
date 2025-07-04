package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PretRepository extends JpaRepository<Pret, Integer> {
        List<Pret> findByUser(User user);

        List<Pret> findByUserAndLivre_TitreContainingIgnoreCase(User user, String titre);

        @Query("SELECT COALESCE(SUM(p.nbr), 0) FROM Pret p WHERE p.user.id = :idUser AND p.isSurPlace = false")
        int countPretsActifs(@Param("idUser") Long idUser);

        @Query("""
                        SELECT DISTINCT p FROM Pret p
                        JOIN StatusPret sp ON sp.pret = p
                        JOIN Status1 s ON sp.status1 = s
                        WHERE p.user.id = :userId
                        AND (:statut IS NULL OR LOWER(s.nom) = LOWER(:statut))
                        ORDER BY p.datePret DESC
                        """)
        List<Pret> findPretsByUserIdAndStatut(
                        @Param("userId") Integer userId,
                        @Param("statut") String statut);

        @Query("""
                        SELECT DISTINCT p FROM Pret p
                        JOIN StatusPret sp ON sp.pret = p
                        JOIN Status1 s ON sp.status1 = s
                        WHERE (:statut IS NULL OR LOWER(s.nom) = LOWER(:statut))
                        ORDER BY p.datePret DESC
                        """)
        List<Pret> findAllPretsByStatut(@Param("statut") String statut);

        @Query("""
                            SELECT DISTINCT p FROM Pret p
                            JOIN p.user u
                            JOIN StatusPret sp ON sp.pret = p
                            WHERE (:statut IS NULL OR sp.status1.nom = :statut)
                              AND (:etudiant IS NULL OR LOWER(u.nom) LIKE LOWER(CONCAT('%', :etudiant, '%'))
                                  OR LOWER(u.prenom) LIKE LOWER(CONCAT('%', :etudiant, '%')))
                        """)
        List<Pret> findByStatutAndEtudiant(@Param("statut") String statut, @Param("etudiant") String etudiant);

        @Query("SELECT DISTINCT sp.pret FROM StatusPret sp WHERE sp.status1.nom = :nomStatus")
        List<Pret> findPretsByStatusNom(@Param("nomStatus") String nomStatus);

        @Query("""
                            SELECT DISTINCT sp.pret FROM StatusPret sp
                            WHERE (:nomStatus IS NULL OR sp.status1.nom = :nomStatus)
                              AND (:etudiant IS NULL OR LOWER(sp.pret.user.nom) LIKE LOWER(CONCAT('%', :etudiant, '%'))
                                   OR LOWER(sp.pret.user.prenom) LIKE LOWER(CONCAT('%', :etudiant, '%')))
                        """)
        List<Pret> findPretsByStatusEtudiant(@Param("nomStatus") String nomStatus, @Param("etudiant") String etudiant);

}
