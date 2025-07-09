package com.agrostock.repository;

import com.agrostock.model.ProduitFini;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProduitFiniRepository extends JpaRepository<ProduitFini, String> {
    ProduitFini findByCode(String code);
}