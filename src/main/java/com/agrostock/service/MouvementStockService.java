package com.agrostock.service;

import com.agrostock.model.Client;
import com.agrostock.model.Fournisseur;
import com.agrostock.model.MatierePremiere;
import com.agrostock.model.MouvementStock;
import com.agrostock.model.MouvementStock.TypeMouvement;
import com.agrostock.repository.MouvementStockRepository;
import com.agrostock.repository.FournisseurRepository;
import com.agrostock.repository.ClientRepository;
import com.agrostock.repository.MatierePremiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MouvementStockService {

    @Autowired
    private MouvementStockRepository repo;
    @Autowired
    private FournisseurRepository fournisseurRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private MatierePremiereRepository matiereRepo;

    public List<MouvementStock> findAll() {
        return repo.findAll();
    }

    public MouvementStock save(MouvementStock mouvement) {
        return repo.save(mouvement);
    }

    public List<MouvementStock> findByProduitCode(String produitCode) {
        return repo.findByProduitCodeOrderByDateDesc(produitCode);
    }

    public List<MouvementStock> findEntreesSince(LocalDateTime date) {
        return repo.findByTypeAndDateGreaterThanEqual(TypeMouvement.ENTREE, date);
    }

    public List<MouvementStock> findByOperationId(Integer operationId) {
        return repo.findByOperationCreditId(operationId);
    }

    public MouvementStock findById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    public Fournisseur findFournisseurById(Integer id) {
        return fournisseurRepo.findById(id).orElse(null);
    }

    public Client findClientById(Integer id) {
        return clientRepo.findById(id).orElse(null);
    }

    public boolean existsProduit(String produitCode, MouvementStock.ProduitType produitType) {
        switch (produitType) {
            case MATIERE_PREMIERE:
                return matiereRepo.existsByCode(produitCode);
            case PRODUIT_FINI:
            case PRODUCTION:
                return repo.existsByProduitCode(produitCode);
            default:
                return false;
        }
    }

    public Double getPrixUnitaire(String produitCode, MouvementStock.ProduitType produitType) {
        switch (produitType) {
            case MATIERE_PREMIERE:
                return matiereRepo.findByCode(produitCode).map(MatierePremiere::getPrixUnitaire).orElse(null);
            case PRODUIT_FINI:
            case PRODUCTION:
                return null;
            default:
                return null;
        }
    }

    public Double getQuantiteStock(String produitCode, MouvementStock.ProduitType produitType) {
        switch (produitType) {
            case MATIERE_PREMIERE:
                return matiereRepo.findByCode(produitCode).map(MatierePremiere::getQuantiteStock).orElse(null);
            case PRODUIT_FINI:
            case PRODUCTION:
                return null;
            default:
                return null;
        }
    }
}