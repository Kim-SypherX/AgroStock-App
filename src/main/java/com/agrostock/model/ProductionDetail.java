package com.agrostock.model;

import jakarta.persistence.*;

@Entity
public class ProductionDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "production_id", nullable = false)
    private Production production;

    @ManyToOne
    @JoinColumn(name = "matiere_premiere_code", referencedColumnName = "code", nullable = false)
    private MatierePremiere matierePremiere;

    private Double quantiteUtilisee;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Production getProduction() { return production; }
    public void setProduction(Production production) { this.production = production; }

    public MatierePremiere getMatierePremiere() { return matierePremiere; }
    public void setMatierePremiere(MatierePremiere matierePremiere) { this.matierePremiere = matierePremiere; }

    public Double getQuantiteUtilisee() { return quantiteUtilisee; }
    public void setQuantiteUtilisee(Double quantiteUtilisee) { this.quantiteUtilisee = quantiteUtilisee; }
}