package com.agrostock.service;

import com.agrostock.model.Inventaire;
import com.agrostock.repository.InventaireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventaireService {

    @Autowired
    private InventaireRepository repo;

    public List<Inventaire> findAll() {
        return repo.findAllByOrderByDateDesc();
    }

    public Inventaire save(Inventaire inventaire) {
        return repo.save(inventaire);
    }

    public Inventaire findById(Integer id) {
        return repo.getById(id); // Utilise getById pour charger avec les relations
    }

    public List<Inventaire> findByResponsable(String responsable) {
        return repo.findByResponsableOrderByDateDesc(responsable);
    }

    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}