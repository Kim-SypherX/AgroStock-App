package com.agrostock.controller;

import com.agrostock.model.Fournisseur;
import com.agrostock.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/fournisseurs")
public class FournisseurController {

    @Autowired
    private FournisseurService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("fournisseurs", service.findAll());
        model.addAttribute("activePage", "/fournisseurs");
        return "fournisseurs";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("fournisseur", new Fournisseur());
        model.addAttribute("activePage", "/fournisseurs");
        return "add-fournisseur";
    }

    @PostMapping
    public String add(@ModelAttribute Fournisseur fournisseur, Model model) {
        try {
            service.save(fournisseur);
            return "redirect:/fournisseurs";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            model.addAttribute("activePage", "/fournisseurs");
            return "add-fournisseur";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/fournisseurs";
    }
}