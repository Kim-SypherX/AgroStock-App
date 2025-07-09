package com.agrostock.service;

import com.agrostock.model.MatierePremiere;
import com.agrostock.repository.MatierePremiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class MatierePremiereService {

    @Autowired
    private MatierePremiereRepository repo;

    public List<MatierePremiere> findAll() {
        return repo.findAll();
    }

    public MatierePremiere save(MatierePremiere matiere) {
        return repo.save(matiere);
    }

    public Optional<MatierePremiere> findByCode(String code) {
        return repo.findByCode(code);
    }

    public void deleteByCode(String code) {
        if (repo.existsByCode(code)) {
            repo.deleteByCode(code);
        } else {
            throw new IllegalArgumentException("Aucune matière première trouvée avec le code : " + code);
        }
    }

    public long count() {
        return repo.count(); // Méthode fournie par JpaRepository
    }

    public String generateCode(String type) {
        if (!Pattern.matches("[A-Z]{3}", type)) {
            throw new IllegalArgumentException("Type doit être un code de 3 lettres (ex: MAG)");
        }
        int sequence = 1;
        String baseCode = type.toUpperCase();
        String code;
        do {
            code = String.format("%s%03d", baseCode, sequence++);
        } while (repo.findByCode(code).isPresent());
        return code;
    }
}