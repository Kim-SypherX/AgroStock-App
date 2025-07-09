package com.agrostock.service;

import com.agrostock.model.Production;
import com.agrostock.repository.ProductionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductionService {

    @Autowired
    private ProductionRepository repo;

    public List<Production> findAll() {
        return repo.findAll();
    }

    public Production save(Production production) {
        return repo.save(production);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public long count() {
        return entityManager.createQuery("SELECT COUNT(p) FROM Production p", Long.class).getSingleResult();
    }

    public Production findById(Long id) {
        return repo.findById(Math.toIntExact(id)).orElse(null);
    }
}