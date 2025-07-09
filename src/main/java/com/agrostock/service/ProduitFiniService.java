package com.agrostock.service;

import com.agrostock.model.ProduitFini;
import com.agrostock.repository.ProduitFiniRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProduitFiniService {

    @Autowired
    private ProduitFiniRepository repo;

    @PersistenceContext
    private EntityManager entityManager;

    public List<ProduitFini> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des produits finis : " + e.getMessage(), e);
        }
    }

    public ProduitFini save(ProduitFini produit) {
        return repo.save(produit);
    }

    public ProduitFini findByCode(String code) {
        return repo.findByCode(code);
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(p) FROM ProduitFini p", Long.class).getSingleResult();
    }

    public void deleteByCode(String code) {
        ProduitFini produit = findByCode(code);
        if (produit != null) {
            repo.delete(produit);
        }
    }
}