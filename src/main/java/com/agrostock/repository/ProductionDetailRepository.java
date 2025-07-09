package com.agrostock.repository;

import com.agrostock.model.ProductionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionDetailRepository extends JpaRepository<ProductionDetail, Long> {
}