package com.agrostock.controller;

import com.agrostock.model.Client;
import com.agrostock.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clients", service.findAll());
        model.addAttribute("activePage", "/clients");
        return "clients";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("activePage", "/clients");
        return "add-client";
    }

    @PostMapping
    public String add(@ModelAttribute Client client, Model model) {
        try {
            service.save(client);
            return "redirect:/clients";
        } catch (Exception e) {
            model.addAttribute("error", "Erreur lors de l'ajout : " + e.getMessage());
            model.addAttribute("activePage", "/clients");
            return "add-client";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        service.deleteById(id);
        return "redirect:/clients";
    }
}