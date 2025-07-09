package com.agrostock.repository;

import com.agrostock.model.MatierePremiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MatierePremiereRepository extends JpaRepository<MatierePremiere, String> {
    @Query("SELECT m FROM MatierePremiere m WHERE m.code LIKE ?1%")
    List<MatierePremiere> findByCodeStartingWith(String prefix);

    // Recherche exacte par code
    Optional<MatierePremiere> findByCode(String code);

    // Vérification de l'existence par code
    boolean existsByCode(String code);

    // Définition explicite de deleteByCode
    @Query("DELETE FROM MatierePremiere m WHERE m.code = ?1")
    void deleteByCode(String code);
}