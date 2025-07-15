package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Pret;
import com.java.bibliotheque.entite.StatusPret;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusPretRepository extends JpaRepository<StatusPret, Integer> {
    Optional<StatusPret> findTopByPretIdPretOrderByDateActionDesc(Integer pretId);

    List<StatusPret> findByPretOrderByDateActionAsc(Pret pret);

    List<StatusPret> findByPret(Pret pret);

}
