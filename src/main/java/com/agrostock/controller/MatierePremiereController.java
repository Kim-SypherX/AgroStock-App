package com.agrostock.controller;

import com.agrostock.model.Client;
import com.agrostock.model.Fournisseur;
import com.agrostock.model.MatierePremiere;
import com.agrostock.model.MouvementStock;
import com.agrostock.model.MouvementStock.ProduitType;
import com.agrostock.model.MouvementStock.TypeMouvement;
import com.agrostock.service.MatierePremiereService;
import com.agrostock.service.MouvementStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class MatierePremiereController {

    @Autowired
    private MatierePremiereService matiereService;
    @Autowired
    private MouvementStockService mouvementService; // Injecter le service MouvementStock

    @GetMapping("/matieres-premieres")
    public String showMaterielList(Model model) {
        List<MatierePremiere> matieres = matiereService.findAll();
        Collections.reverse(matieres); // Garde l'ordre inverse
        model.addAttribute("matieres", matieres);
        model.addAttribute("title", "Liste des");
        model.addAttribute("activePage", "/matieres-premieres");
        return "matieres-premieres";
    }

    @GetMapping("/matieres-premieres/add")
    public String showMaterielPremiere(Model model) {
        model.addAttribute("matieres", matiereService.findAll());
        model.addAttribute("matiere", new MatierePremiere());
        List<String> types = Arrays.asList("MAG", "PAP", "PAS", "HIB", "CIT", "GIN", "MAN", "SOJ", "MAI", "RIZ", "FDP", "ANA", "JUJ", "KAR", "NER", "DET", "RAI", "BAL", "TAM");
        List<String> typeNames = Arrays.asList("Mangue", "Papaye", "Pastèque", "Hibiscus ou fleur de bissap", "Citron", "Gingembre", "Manioc", "Soja", "Maïs", "Riz", "Fruits de la passion", "Ananas", "Jujube", "Karité", "Néré", "Détariun ou kaga", "Raison tropical", "Balanites ou kegla", "Tamarin");
        List<String> categories = Arrays.asList("Fruit", "Fruit", "Fruit", "Fleur", "Fruit", "Épice", "Tubercule", "Légumineuse", "Céréale", "Céréale", "Fruit", "Fruit", "Fruit", "Noix", "Légume", "Légume", "Fruit", "Fruit", "Fruit");
        model.addAttribute("types", types);
        model.addAttribute("typeNames", typeNames);
        model.addAttribute("categories", categories);
        model.addAttribute("activePage", "/matieres-premieres");
        return "add-matiere";
    }

    @PostMapping("/matieres-premieres")
    public String addMatierePremiere(@ModelAttribute MatierePremiere matiere, Model model) {
        try {
            if (matiere.getType() == null) {
                throw new IllegalArgumentException("Type requis");
            }
            String code = matiereService.generateCode(matiere.getType());
            if (!Pattern.matches("[A-Z]{3}\\d{3}", code)) {
                throw new IllegalArgumentException("Format de code invalide. Attendu: XXX999 (ex: MAG001)");
            }
            matiere.setCode(code);
            matiere.setNom(getTypeName(matiere.getType()));
            matiere.setUnite("kg");
            matiere.setCategorie(getCategory(matiere.getType()));
            if (matiere.getQuantiteStock() == null || matiere.getQuantiteStock() <= 0) {
                throw new IllegalArgumentException("Quantité doit être positive");
            }
            if (matiere.getPrixUnitaire() == null || matiere.getPrixUnitaire() < 0) {
                throw new IllegalArgumentException("Prix unitaire doit être positif");
            }
            if (matiereService.findByCode(code).isPresent()) {
                throw new IllegalArgumentException("Code déjà utilisé, veuillez réessayer.");
            }
            matiereService.save(matiere);

            MouvementStock mouvement = new MouvementStock();
            mouvement.setType(TypeMouvement.ENTREE);
            mouvement.setProduitCode(code);
            mouvement.setProduitType(MouvementStock.ProduitType.MATIERE_PREMIERE);
            mouvement.setQuantite(matiere.getQuantiteStock());
            mouvement.setPrixUnitaire(matiere.getPrixUnitaire());
            mouvement.setDate(LocalDateTime.now());
            mouvement.setMotif("Ajout initial de matière");
            mouvement.setFournisseur(null);
            mouvement.setClient(null);
            mouvementService.save(mouvement);

            return "redirect:/matieres-premieres";
        } catch (Exception e) {
            model.addAttribute("matieres", matiereService.findAll());
            model.addAttribute("matiere", matiere);
            List<String> types = Arrays.asList("MAG", "PAP", "PAS", "HIB", "CIT", "GIN", "MAN", "SOJ", "MAI", "RIZ", "FDP", "ANA", "JUJ", "KAR", "NER", "DET", "RAI", "BAL", "TAM");
            List<String> typeNames = Arrays.asList("Mangue", "Papaye", "Pastèque", "Hibiscus ou fleur de bissap", "Citron", "Gingembre", "Manioc", "Soja", "Maïs", "Riz", "Fruits de la passion", "Ananas", "Jujube", "Karité", "Néré", "Détariun ou kaga", "Raison tropical", "Balanites ou kegla", "Tamarin");
            List<String> categories = Arrays.asList("Fruit", "Fruit", "Fruit", "Fleur", "Fruit", "Épice", "Tubercule", "Légumineuse", "Céréale", "Céréale", "Fruit", "Fruit", "Fruit", "Noix", "Légume", "Légume", "Fruit", "Fruit", "Fruit");
            model.addAttribute("types", types);
            model.addAttribute("typeNames", typeNames);
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Erreur : " + e.getMessage());
            model.addAttribute("activePage", "/matieres-premieres");
            return "add-matiere";
        }
    }

    @GetMapping("/matieres-premieres/edit/{code}")
    public String showEditForm(@PathVariable("code") String code, Model model) {
        Optional<MatierePremiere> matiere = matiereService.findByCode(code);
        if (matiere == null) {
            return "redirect:/matieres-premieres";
        }
        model.addAttribute("matiere", matiere);
        List<String> types = Arrays.asList("MAG", "PAP", "PAS", "HIB", "CIT", "GIN", "MAN", "SOJ", "MAI", "RIZ", "FDP", "ANA", "JUJ", "KAR", "NER", "DET", "RAI", "BAL", "TAM");
        List<String> typeNames = Arrays.asList("Mangue", "Papaye", "Pastèque", "Hibiscus ou fleur de bissap", "Citron", "Gingembre", "Manioc", "Soja", "Maïs", "Riz", "Fruits de la passion", "Ananas", "Jujube", "Karité", "Néré", "Détariun ou kaga", "Raison tropical", "Balanites ou kegla", "Tamarin");
        List<String> categories = Arrays.asList("Fruit", "Fruit", "Fruit", "Fleur", "Fruit", "Épice", "Tubercule", "Légumineuse", "Céréale", "Céréale", "Fruit", "Fruit", "Fruit", "Noix", "Légume", "Légume", "Fruit", "Fruit", "Fruit");
        model.addAttribute("types", types);
        model.addAttribute("typeNames", typeNames);
        model.addAttribute("categories", categories);
        model.addAttribute("activePage", "/matieres-premieres");
        return "edit-matiere";
    }

    @PostMapping("/matieres-premieres/update")
    public String updateMatierePremiere(@ModelAttribute MatierePremiere matiere, Model model) {
        try {
            if (matiere.getQuantiteStock() == null || matiere.getQuantiteStock() < 0) {
                throw new IllegalArgumentException("Quantité doit être positive");
            }
            if (matiere.getPrixUnitaire() == null || matiere.getPrixUnitaire() < 0) {
                throw new IllegalArgumentException("Prix unitaire doit être positif");
            }
            matiere.setCategorie(getCategory(matiere.getType()));
            matiere.setUnite("kg");
            matiereService.save(matiere);
            return "redirect:/matieres-premieres";
        } catch (Exception e) {
            model.addAttribute("matiere", matiere);
            List<String> types = Arrays.asList("MAG", "PAP", "PAS", "HIB", "CIT", "GIN", "MAN", "SOJ", "MAI", "RIZ", "FDP", "ANA", "JUJ", "KAR", "NER", "DET", "RAI", "BAL", "TAM");
            List<String> typeNames = Arrays.asList("Mangue", "Papaye", "Pastèque", "Hibiscus ou fleur de bissap", "Citron", "Gingembre", "Manioc", "Soja", "Maïs", "Riz", "Fruits de la passion", "Ananas", "Jujube", "Karité", "Néré", "Détariun ou kaga", "Raison tropical", "Balanites ou kegla", "Tamarin");
            List<String> categories = Arrays.asList("Fruit", "Fruit", "Fruit", "Fleur", "Fruit", "Épice", "Tubercule", "Légumineuse", "Céréale", "Céréale", "Fruit", "Fruit", "Fruit", "Noix", "Légume", "Légume", "Fruit", "Fruit", "Fruit");
            model.addAttribute("types", types);
            model.addAttribute("typeNames", typeNames);
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Erreur : " + e.getMessage());
            model.addAttribute("activePage", "/matieres-premieres");
            return "edit-matiere";
        }
    }

    @GetMapping("/matieres-premieres/delete/{code}")
    public String deleteMatierePremiere(@PathVariable("code") String code, Model model) {
        matiereService.deleteByCode(code);
        return "redirect:/matieres-premieres";
    }

    private String getTypeName(String type) {
        String[] typeNames = {"Mangue", "Papaye", "Pastèque", "Hibiscus ou fleur de bissap", "Citron", "Gingembre", "Manioc", "Soja", "Maïs", "Riz", "Fruits de la passion", "Ananas", "Jujube", "Karité", "Néré", "Détariun ou kaga", "Raison tropical", "Balanites ou kegla", "Tamarin"};
        String[] types = {"MAG", "PAP", "PAS", "HIB", "CIT", "GIN", "MAN", "SOJ", "MAI", "RIZ", "FDP", "ANA", "JUJ", "KAR", "NER", "DET", "RAI", "BAL", "TAM"};
        for (int i = 0; i < types.length; i++) if (types[i].equals(type)) return typeNames[i];
        return type;
    }

    private String getCategory(String type) {
        String[] categories = {"Fruit", "Fruit", "Fruit", "Fleur", "Fruit", "Épice", "Tubercule", "Légumineuse", "Céréale", "Céréale", "Fruit", "Fruit", "Fruit", "Noix", "Légume", "Légume", "Fruit", "Fruit", "Fruit"};
        String[] types = {"MAG", "PAP", "PAS", "HIB", "CIT", "GIN", "MAN", "SOJ", "MAI", "RIZ", "FDP", "ANA", "JUJ", "KAR", "NER", "DET", "RAI", "BAL", "TAM"};
        for (int i = 0; i < types.length; i++) if (types[i].equals(type)) return categories[i];
        return "Inconnu";
    }
}