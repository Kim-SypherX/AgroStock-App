package com.agrostock.service;

import com.agrostock.model.Client;
import com.agrostock.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repo;

    public List<Client> findAll() {
        try {
            return repo.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des clients : " + e.getMessage(), e);
        }
    }

    public Client save(Client client) {
        try {
            return repo.save(client);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du client : " + e.getMessage(), e);
        }
    }

    public void deleteById(Integer id) {
        try {
            repo.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression du client : " + e.getMessage(), e);
        }
    }
}