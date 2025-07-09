package com.agrostock.controller;

import com.agrostock.model.Inventaire;
import com.agrostock.model.LigneInventaire;
import com.agrostock.service.LigneInventaireService;
import com.agrostock.service.InventaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/lignes-inventaire")
public class LigneInventaireController {

    @Autowired
    private LigneInventaireService service;

    @Autowired
    private InventaireService inventaireService;

    @GetMapping
    public String list(Model model) {
        try {
            model.addAttribute("lignesInventaire", service.findAll());
            model.addAttribute("activePage", "/lignes-inventaire");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors du chargement des lignes d'inventaire : " + e.getMessage());
            model.addAttribute("lignesInventaire", java.util.Collections.emptyList());
        }
        return "lignes-inventaire";
    }



    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("ligneInventaire", new LigneInventaire());
        model.addAttribute("activePage", "/lignes-inventaire");
        model.addAttribute("inventaires", inventaireService.findAll());
        return "add-ligne";
    }

    @PostMapping
    public String add(@ModelAttribute LigneInventaire ligneInventaire, Model model) {
        try {
            // Charger l'inventaire à partir de l'ID
            if (ligneInventaire.getInventaire() != null && ligneInventaire.getInventaire().getId() != null) {
                Inventaire inventaire = inventaireService.findById(ligneInventaire.getInventaire().getId());
                if (inventaire == null) {
                    throw new IllegalArgumentException("Inventaire non trouvé avec ID : " + ligneInventaire.getInventaire().getId());
                }
                ligneInventaire.setInventaire(inventaire);
            }
            service.save(ligneInventaire);
            return "redirect:/lignes-inventaire";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            model.addAttribute("activePage", "/lignes-inventaire");
            model.addAttribute("inventaires", inventaireService.findAll());
            return "add-ligne";
        }
    }

    @GetMapping("/by-inventaire/{id}")
    public String listByInventaire(@PathVariable Integer id, Model model) {
        try {
            model.addAttribute("lignesInventaire", service.findByInventaire(id));
            model.addAttribute("activePage", "/lignes-inventaire");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("lignesInventaire", java.util.Collections.emptyList());
            model.addAttribute("activePage", "/lignes-inventaire");
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la recherche : " + e.getMessage());
            model.addAttribute("lignesInventaire", java.util.Collections.emptyList());
            model.addAttribute("activePage", "/lignes-inventaire");
        }
        return "lignes-inventaire";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/lignes-inventaire";
    }
}