package com.agrostock.controller;

import com.agrostock.model.Production;
import com.agrostock.model.ProduitFini;
import com.agrostock.service.ProductionService;
import com.agrostock.service.ProduitFiniService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produits-finis")
public class ProduitFiniController {

    @Autowired
    private ProduitFiniService service;
    @Autowired
    private ProductionService productionService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("produits", service.findAll());
            model.addAttribute("activePage", "/produits-finis");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la récupération des produits finis : " + e.getMessage());
        }
        return "produits-finis";
    }


    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("produit", new ProduitFini());
        model.addAttribute("productions", productionService.findAll());
        model.addAttribute("activePage", "/produits-finis");
        return "add-produit"; // On va ajuster add-produit.html
    }

    @PostMapping
    public String add(@ModelAttribute ProduitFini produit, @RequestParam("productionId") Long productionId, Model model) {
        try {
            System.out.println("Ajout de produit fini - productionId: " + productionId + ", quantiteProduite: " + produit.getQuantiteProduite());
            Production production = productionService.findById(productionId);
            if (production == null) {
                throw new IllegalArgumentException("Production introuvable avec ID: " + productionId);
            }
            produit.setProduction(production);
            produit.setNom(production.getNom());
            produit.setCategorie("Fini");
            String prefix = "JUS"; // À ajuster selon le type de produit
            String code = String.format("PF-%s-%03d", prefix, productionId); // Ex: PF-JUS-001
            System.out.println("Génération du code: " + code);
            produit.setCode(code);
            if (produit.getQuantiteProduite() == null || produit.getQuantiteProduite() <= 0) {
                throw new IllegalArgumentException("Quantité produite requise et positive: " + produit.getQuantiteProduite());
            }
            // Copier quantiteProduite dans quantiteStock
            produit.setQuantiteStock(produit.getQuantiteProduite());
            // Définir prixUnitaire à 0.00 par défaut (optionnel)
            if (produit.getPrixUnitaire() == null) {
                produit.setPrixUnitaire(0.00);
            }
            ProduitFini savedProduit = service.save(produit);
            System.out.println("Produit fini sauvegardé avec code: " + savedProduit.getCode());
            return "redirect:/produits-finis";
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout - Détail: " + e.getMessage());
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            model.addAttribute("produit", produit);
            model.addAttribute("productions", productionService.findAll());
            return "add-produit";
        }
    }

    @GetMapping("/edit/{code}")
    public String showEditForm(@PathVariable String code, Model model) {
        ProduitFini produit = service.findByCode(code); // Assume un findByCode dans le service
        if (produit == null) {
            model.addAttribute("error", "Produit fini non trouvé avec code : " + code);
            return "redirect:/produits-finis";
        }
        model.addAttribute("produit", produit);
        return "edit-produit";
    }

    @PostMapping("/update")
    public String updateProduit(@ModelAttribute("produit") ProduitFini produit, Model model) {
        try {
            System.out.println("Mise à jour du produit - code: " + produit.getCode() + ", quantiteProduite: " + produit.getQuantiteProduite() + ", prixUnitaire: " + produit.getPrixUnitaire());
            if (produit.getCode() == null || produit.getCode().trim().isEmpty()) {
                throw new IllegalArgumentException("Le code du produit est requis");
            }
            if (produit.getQuantiteProduite() != null && produit.getQuantiteProduite() <= 0) {
                throw new IllegalArgumentException("La quantité produite doit être positive");
            }
            ProduitFini existingProduit = service.findByCode(produit.getCode());
            if (existingProduit == null) {
                throw new IllegalArgumentException("Produit fini non trouvé avec code : " + produit.getCode());
            }
            // Mettre à jour les champs modifiables
            existingProduit.setNom(produit.getNom());
            existingProduit.setCategorie(produit.getCategorie());
            existingProduit.setQuantiteProduite(produit.getQuantiteProduite());
            existingProduit.setPrixUnitaire(produit.getPrixUnitaire());
            existingProduit.setQuantiteStock(produit.getQuantiteStock()); // À ajuster selon la logique
            service.save(existingProduit);
            System.out.println("Produit fini mis à jour avec code: " + existingProduit.getCode());
            return "redirect:/produits-finis";
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour - Détail: " + e.getMessage());
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            model.addAttribute("produit", produit);
            return "edit-produit";
        }
    }

    @GetMapping("/delete/{code}")
    public String delete(@PathVariable String code) {
        try {
            service.deleteByCode(code);
        } catch (Exception e) {
            // Log l'erreur si nécessaire
        }
        return "redirect:/produits-finis";
    }
}