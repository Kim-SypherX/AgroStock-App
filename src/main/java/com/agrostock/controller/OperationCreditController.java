package com.agrostock.controller;

import com.agrostock.model.OperationCredit;
import com.agrostock.service.ClientService;
import com.agrostock.service.FournisseurService;
import com.agrostock.service.OperationCreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/operations-credit")
public class OperationCreditController {

    @Autowired
    private OperationCreditService service;

    @Autowired
    private FournisseurService fournisseurService;

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("operations", service.findAll());
        model.addAttribute("activePage", "/operations-credit");
        return "operations-credit";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("operation", new OperationCredit());
        model.addAttribute("fournisseurs", fournisseurService.findAll());
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("activePage", "/operations-credit");
        return "add-operation-credit";
    }

    @PostMapping
    public String add(@ModelAttribute OperationCredit operation, Model model) {
        try {
            service.save(operation);
            return "redirect:/operations-credit";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            model.addAttribute("activePage", "/operations-credit");
            return "add-operation-credit";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/operations-credit";
    }
}