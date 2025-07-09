package com.agrostock.repository;

import com.agrostock.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitRepository extends JpaRepository<Produit, String> {
    boolean existsByCodeAndType(String code, String type);
    Produit findByCodeAndType(String code, String type);
}