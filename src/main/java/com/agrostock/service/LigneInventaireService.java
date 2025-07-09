package com.agrostock.service;

import com.agrostock.model.Inventaire;
import com.agrostock.model.LigneInventaire;
import com.agrostock.repository.LigneInventaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LigneInventaireService {

    @Autowired
    private LigneInventaireRepository repo;

    @Autowired
    private InventaireService inventaireService;

    public List<LigneInventaire> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des lignes d'inventaire : " + e.getMessage(), e);
        }
    }

    public LigneInventaire save(LigneInventaire ligne) {
        try {
            return repo.save(ligne);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de la ligne d'inventaire : " + e.getMessage(), e);
        }
    }

    public LigneInventaire findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public List<LigneInventaire> findByInventaire(Integer inventaireId) {
        try {
            Inventaire inventaire = inventaireService.findById(inventaireId);
            if (inventaire == null) {
                throw new IllegalArgumentException("Inventaire non trouvé avec ID : " + inventaireId);
            }
            return repo.findByInventaire(inventaire);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par inventaire : " + e.getMessage(), e);
        }
    }

    public List<LigneInventaire> findByProduitCode(String produitCode) {
        try {
            return repo.findByProduitCode(produitCode);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la recherche par produit : " + e.getMessage(), e);
        }
    }

    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression : " + e.getMessage(), e);
        }
    }
}