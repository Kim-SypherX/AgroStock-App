package com.agrostock.repository;

import com.agrostock.model.Production;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionRepository extends JpaRepository<Production, Integer> {
}