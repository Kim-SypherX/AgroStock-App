package com.agrostock.repository;

import com.agrostock.model.Inventaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventaireRepository extends JpaRepository<Inventaire, Integer> {
    List<Inventaire> findByResponsableOrderByDateDesc(String responsable);

    @Override
    Inventaire getById(Integer id); // Remplace findById avec getById pour un accès direct

    List<Inventaire> findAllByOrderByDateDesc();
}