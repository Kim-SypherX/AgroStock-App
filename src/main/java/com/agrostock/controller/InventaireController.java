package com.agrostock.controller;

import com.agrostock.model.Inventaire;
import com.agrostock.service.InventaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventaires")
public class InventaireController {

    @Autowired
    private InventaireService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("inventaires", service.findAll());
        model.addAttribute("activePage", "/inventaires"); // Ajoute activePage pour la navigation
        return "inventaires";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("inventaire", new Inventaire());
        model.addAttribute("activePage", "/inventaires"); // Ajoute activePage
        return "add-inventaire";
    }

    @PostMapping
    public String add(@ModelAttribute Inventaire inventaire, Model model) {
        try {
            service.save(inventaire);
            return "redirect:/inventaires";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            return "add-inventaire";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Inventaire inventaire = service.findById(id);
        if (inventaire == null) {
            return "redirect:/inventaires";
        }
        model.addAttribute("inventaire", inventaire);
        model.addAttribute("activePage", "/inventaires"); // Ajoute activePage
        return "edit-inventaire";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Inventaire inventaire, Model model) {
        try {
            service.save(inventaire);
            return "redirect:/inventaires";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de la mise à jour : " + e.getMessage());
            return "edit-inventaire";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/inventaires";
    }

    @GetMapping("/by-responsable")
    public String listByResponsable(@RequestParam String responsable, Model model) {
        model.addAttribute("inventaires", service.findByResponsable(responsable));
        model.addAttribute("activePage", "/inventaires"); // Ajoute activePage
        return "inventaires";
    }
}