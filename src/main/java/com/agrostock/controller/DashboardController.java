package com.agrostock.controller;

import com.agrostock.service.MatierePremiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @Autowired
    private MatierePremiereService matierePremiereService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        long matiereCount = matierePremiereService.count(); // Utilisation de count()
        model.addAttribute("matiereCount", matiereCount);
        model.addAttribute("activePage", "/dashboard");
        return "dashboard";
    }
}