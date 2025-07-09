package com.agrostock.service;

import com.agrostock.model.Fournisseur;
import com.agrostock.repository.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FournisseurService {

    @Autowired
    private FournisseurRepository repo;

    public List<Fournisseur> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des fournisseurs : " + e.getMessage(), e);
        }
    }

    public Fournisseur save(Fournisseur fournisseur) {
        try {
            return repo.save(fournisseur);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fournisseur : " + e.getMessage(), e);
        }
    }

    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du fournisseur : " + e.getMessage(), e);
        }
    }
}