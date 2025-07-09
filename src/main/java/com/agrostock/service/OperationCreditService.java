package com.agrostock.service;

import com.agrostock.model.OperationCredit;
import com.agrostock.repository.OperationCreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationCreditService {

    @Autowired
    private OperationCreditRepository repo;

    public List<OperationCredit> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des opérations de crédit : " + e.getMessage(), e);
        }
    }

    public OperationCredit save(OperationCredit operation) {
        try {
            return repo.save(operation);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'opération de crédit : " + e.getMessage(), e);
        }
    }

    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression de l'opération de crédit : " + e.getMessage(), e);
        }
    }
}