package com.agrostock.repository;

import com.agrostock.model.MouvementStock;
import com.agrostock.model.MouvementStock.TypeMouvement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Integer> {
    List<MouvementStock> findByProduitCodeOrderByDateDesc(String produitCode);

    List<MouvementStock> findByTypeAndDateGreaterThanEqual(TypeMouvement type, LocalDateTime date);

    List<MouvementStock> findByOperationCreditId(Integer operationId);

    boolean existsByProduitCode(String produitCode);
}