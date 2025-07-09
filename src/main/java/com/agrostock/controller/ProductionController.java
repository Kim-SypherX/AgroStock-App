package com.agrostock.controller;

import com.agrostock.model.Production;
import com.agrostock.model.ProductionDetail;
import com.agrostock.model.MatierePremiere;
import com.agrostock.model.MouvementStock;
import com.agrostock.model.MouvementStock.TypeMouvement;
import com.agrostock.model.MouvementStock.ProduitType;
import com.agrostock.service.MatierePremiereService;
import com.agrostock.service.ProductionService;
import com.agrostock.service.MouvementStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductionController {

    @Autowired
    private ProductionService service;
    @Autowired
    private MatierePremiereService matiereService;
    @Autowired
    private MouvementStockService mouvementService;

    @GetMapping("/production")
    public String showProductionForm(Model model) {
        model.addAttribute("production", new Production());
        model.addAttribute("detail", new ProductionDetail());
        model.addAttribute("matieres", matiereService.findAll());
        model.addAttribute("productions", service.findAll());
        model.addAttribute("activePage", "/production");
        return "production";
    }

    @PostMapping("/production")
    public String addProduction(@ModelAttribute Production production, @ModelAttribute("detail") ProductionDetail detail, Model model) {
        try {
            if (production.getNom() == null || production.getNom().trim().isEmpty()) {
                throw new IllegalArgumentException("Nom de la production requis");
            }
            if (production.getDetails() == null) {
                production.setDetails(new ArrayList<>());
            }
            if (production.getDetails().isEmpty()) {
                throw new IllegalArgumentException("Au moins une matière première est requise");
            }
            List<ProductionDetail> details = new ArrayList<>();
            for (ProductionDetail d : production.getDetails()) {
                if (d.getMatierePremiere() == null || d.getMatierePremiere().getCode() == null) {
                    throw new IllegalArgumentException("Matière première requise");
                }
                if (d.getQuantiteUtilisee() == null || d.getQuantiteUtilisee() <= 0) {
                    throw new IllegalArgumentException("Quantité requise et positive pour " + d.getMatierePremiere().getCode());
                }
                // Gérer l'Optional retourné par findByCode
                MatierePremiere matiere = matiereService.findByCode(d.getMatierePremiere().getCode())
                        .orElseThrow(() -> new IllegalArgumentException("Matière introuvable: " + d.getMatierePremiere().getCode()));
                if (matiere.getQuantiteStock() < d.getQuantiteUtilisee()) {
                    throw new IllegalArgumentException("Stock insuffisant pour " + matiere.getNom() + " (" + matiere.getQuantiteStock() + ")");
                }
                matiere.setQuantiteStock(matiere.getQuantiteStock() - d.getQuantiteUtilisee());
                matiereService.save(matiere);
                ProductionDetail newDetail = new ProductionDetail();
                newDetail.setMatierePremiere(matiere);
                newDetail.setQuantiteUtilisee(d.getQuantiteUtilisee());
                newDetail.setProduction(production);
                details.add(newDetail);

                MouvementStock mouvementSortie = new MouvementStock();
                mouvementSortie.setType(TypeMouvement.SORTIE);
                mouvementSortie.setProduitCode(matiere.getCode());
                mouvementSortie.setProduitType(ProduitType.MATIERE_PREMIERE);
                mouvementSortie.setQuantite(d.getQuantiteUtilisee());
                mouvementSortie.setPrixUnitaire(matiere.getPrixUnitaire());
                mouvementSortie.setDate(LocalDateTime.now());
                mouvementSortie.setMotif("Utilisation pour production: " + production.getNom());
                mouvementService.save(mouvementSortie);
            }
            production.setDate(LocalDateTime.now());
            production.setDetails(details);
            service.save(production);

            MouvementStock mouvementEntree = new MouvementStock();
            mouvementEntree.setType(TypeMouvement.ENTREE);
            mouvementEntree.setProduitCode(production.getNom());
            mouvementEntree.setProduitType(ProduitType.PRODUIT_FINI);
            mouvementEntree.setQuantite(1.0);
            mouvementEntree.setPrixUnitaire(calculateProduitFiniPrix(production));
            mouvementEntree.setDate(LocalDateTime.now());
            mouvementEntree.setMotif("Production de: " + production.getNom());
            mouvementService.save(mouvementEntree);

            MouvementStock mouvementProduction = new MouvementStock();
            mouvementProduction.setType(TypeMouvement.ENTREE);
            mouvementProduction.setProduitCode(production.getNom());
            mouvementProduction.setProduitType(ProduitType.PRODUCTION);
            mouvementProduction.setQuantite(1.0);
            mouvementProduction.setPrixUnitaire(calculateProduitFiniPrix(production));
            mouvementProduction.setDate(LocalDateTime.now());
            mouvementProduction.setMotif("Enregistrement production: " + production.getNom());
            mouvementService.save(mouvementProduction);

            return "redirect:/production/" + production.getId() + "/details";
        } catch (Exception e) {
            model.addAttribute("productions", service.findAll());
            model.addAttribute("production", production);
            model.addAttribute("matieres", matiereService.findAll());
            model.addAttribute("detail", detail);
            model.addAttribute("error", "Erreur : " + e.getMessage());
            model.addAttribute("activePage", "/production");
            return "production";
        }
    }

    @GetMapping("/production/{id}/details")
    public String showProductionDetails(@PathVariable Integer id, Model model) {
        Production production = service.findById(Long.valueOf(id));
        if (production == null) {
            return "redirect:/production";
        }
        model.addAttribute("production", production);
        model.addAttribute("title", "Détails de la Production");
        model.addAttribute("activePage", "/production");
        return "produit-details";
    }

    private Double calculateProduitFiniPrix(Production production) {
        if (production == null || production.getDetails() == null) return 0.0;
        return production.getDetails().stream()
                .mapToDouble(d -> d.getQuantiteUtilisee() * d.getMatierePremiere().getPrixUnitaire())
                .sum();
    }
}