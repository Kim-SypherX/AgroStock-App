package com.agrostock.repository;

import com.agrostock.model.LigneInventaire;
import com.agrostock.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LigneInventaireRepository extends JpaRepository<LigneInventaire, Integer> {
    List<LigneInventaire> findByInventaire(Inventaire inventaire);
    List<LigneInventaire> findByProduitCode(String produitCode);
}