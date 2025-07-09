package com.agrostock.controller;

import com.agrostock.model.Client;
import com.agrostock.model.Fournisseur;
import com.agrostock.model.MouvementStock;
import com.agrostock.model.MouvementStock.TypeMouvement;
import com.agrostock.service.MouvementStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/mouvements")
public class MouvementStockController {

    @Autowired
    private MouvementStockService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("mouvements", service.findAll());
        model.addAttribute("activePage", "/mouvements");
        return "mouvements";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        MouvementStock mouvement = new MouvementStock();
        mouvement.setFournisseur(new Fournisseur()); // Initialiser fournisseur
        mouvement.setClient(new Client());           // Initialiser client
        model.addAttribute("mouvement", mouvement);
        model.addAttribute("types", TypeMouvement.values());
        model.addAttribute("activePage", "/mouvements");
        return "add-mouvement";
    }

    @PostMapping
    public String add(@ModelAttribute MouvementStock mouvement, Model model) {
        try {
            if (mouvement.getType() == null) {
                throw new IllegalArgumentException("Le type est requis");
            }
            if (mouvement.getQuantite() == null || mouvement.getQuantite() <= 0) {
                throw new IllegalArgumentException("La quantité doit être supérieure à 0");
            }
            // Valider produit_code dans la table produit
            if (!service.existsProduit(mouvement.getProduitCode(), mouvement.getProduitType())) {
                throw new IllegalArgumentException("Le produit avec code " + mouvement.getProduitCode() + " n'existe pas");
            }
            // Récupérer le prix_unitaire du produit
            Double prixUnitaire = service.getPrixUnitaire(mouvement.getProduitCode(), mouvement.getProduitType());
            if (prixUnitaire == null) {
                throw new IllegalArgumentException("Le prix unitaire du produit n'est pas défini");
            }
            mouvement.setPrixUnitaire(prixUnitaire);
            if (mouvement.getType() == TypeMouvement.ENTREE) {
                if (mouvement.getFournisseur() == null || mouvement.getFournisseur().getId() == null) {
                    throw new IllegalArgumentException("Un fournisseur est requis pour une entrée");
                }
                Fournisseur fournisseur = service.findFournisseurById(mouvement.getFournisseur().getId());
                if (fournisseur == null) {
                    throw new IllegalArgumentException("Fournisseur avec ID " + mouvement.getFournisseur().getId() + " non trouvé");
                }
                mouvement.setFournisseur(fournisseur);
                mouvement.setClient(null);
            } else if (mouvement.getType() == TypeMouvement.SORTIE) {
                if (mouvement.getClient() == null || mouvement.getClient().getId() == null) {
                    throw new IllegalArgumentException("Un client est requis pour une sortie");
                }
                Client client = service.findClientById(mouvement.getClient().getId());
                if (client == null) {
                    throw new IllegalArgumentException("Client avec ID " + mouvement.getClient().getId() + " non trouvé");
                }
                mouvement.setClient(client);
                mouvement.setFournisseur(null);
            }
            mouvement.setDate(LocalDateTime.now());
            service.save(mouvement);
            return "redirect:/mouvements";
        } catch (Exception e) {
            mouvement.setFournisseur(mouvement.getFournisseur() != null ? mouvement.getFournisseur() : new Fournisseur());
            mouvement.setClient(mouvement.getClient() != null ? mouvement.getClient() : new Client());
            model.addAttribute("mouvement", mouvement);
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            model.addAttribute("types", TypeMouvement.values());
            model.addAttribute("activePage", "/mouvements");
            return "add-mouvement";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        MouvementStock mouvement = service.findById(id);
        if (mouvement == null) {
            return "redirect:/mouvements";
        }
        if (mouvement.getFournisseur() == null) mouvement.setFournisseur(new Fournisseur());
        if (mouvement.getClient() == null) mouvement.setClient(new Client());
        model.addAttribute("mouvement", mouvement);
        model.addAttribute("types", TypeMouvement.values());
        model.addAttribute("activePage", "/mouvements");
        return "edit-mouvement";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MouvementStock mouvement, Model model) {
        try {
            if (mouvement.getType() == null) {
                throw new IllegalArgumentException("Le type est requis");
            }
            if (mouvement.getType() == TypeMouvement.ENTREE) {
                if (mouvement.getFournisseur() == null || mouvement.getFournisseur().getId() == null) {
                    throw new IllegalArgumentException("Un fournisseur est requis pour une entrée");
                }
                Fournisseur fournisseur = service.findFournisseurById(mouvement.getFournisseur().getId());
                if (fournisseur == null) {
                    throw new IllegalArgumentException("Fournisseur avec ID " + mouvement.getFournisseur().getId() + " non trouvé");
                }
                mouvement.setFournisseur(fournisseur);
                mouvement.setClient(null);
            } else if (mouvement.getType() == TypeMouvement.SORTIE) {
                if (mouvement.getClient() == null || mouvement.getClient().getId() == null) {
                    throw new IllegalArgumentException("Un client est requis pour une sortie");
                }
                Client client = (Client) service.findClientById(mouvement.getClient().getId());
                if (client == null) {
                    throw new IllegalArgumentException("Client avec ID " + mouvement.getClient().getId() + " non trouvé");
                }
                mouvement.setClient(client);
                mouvement.setFournisseur(null);
            }
            MouvementStock existing = service.findById(mouvement.getId());
            if (existing != null && existing.getDate() != null) {
                mouvement.setDate(existing.getDate());
            } else {
                mouvement.setDate(LocalDateTime.now());
            }
            service.save(mouvement);
            return "redirect:/mouvements";
        } catch (Exception e) {
            MouvementStock newMouvement = service.findById(mouvement.getId());
            if (newMouvement == null) newMouvement = new MouvementStock();
            if (newMouvement.getFournisseur() == null) newMouvement.setFournisseur(new Fournisseur());
            if (newMouvement.getClient() == null) newMouvement.setClient(new Client());
            model.addAttribute("mouvement", newMouvement);
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            model.addAttribute("types", TypeMouvement.values());
            model.addAttribute("activePage", "/mouvements");
            return "edit-mouvement";
        }
    }

    @GetMapping("/by-produit")
    public String listByProduit(@RequestParam String produitCode, Model model) {
        model.addAttribute("mouvements", service.findByProduitCode(produitCode));
        model.addAttribute("activePage", "/mouvements");
        return "mouvements";
    }

    @GetMapping("/entrees-since")
    public String listEntreesSince(@RequestParam String date, Model model) {
        LocalDateTime dateTime = LocalDateTime.parse(date + "T00:00:00");
        model.addAttribute("mouvements", service.findEntreesSince(dateTime));
        model.addAttribute("activePage", "/mouvements");
        return "mouvements";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/mouvements";
    }
}