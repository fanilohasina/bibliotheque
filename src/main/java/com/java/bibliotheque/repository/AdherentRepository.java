// AdherentRepository.java
package com.java.bibliotheque.repository;

import com.java.bibliotheque.entite.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdherentRepository extends JpaRepository<Adherent, Integer> {
}
