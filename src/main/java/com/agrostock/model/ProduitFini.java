package com.agrostock.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "produit_fini")
public class ProduitFini {

    @Id
    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "nom", nullable = false, length = 50)
    private String nom;

    @Column(name = "categorie", nullable = false, length = 20)
    private String categorie;

    @ManyToOne
    @JoinColumn(name = "production_id", nullable = false)
    private Production production;

    @Column(name = "quantite_produite", nullable = false)
    private Double quantiteProduite;

    @Column(name = "prix_unitaire", nullable = true, columnDefinition = "numeric(10,2) default 0.00")
    private Double prixUnitaire;

    @Column(name = "quantite_stock", nullable = true, columnDefinition = "numeric(10,2) default 0.00")
    private Double quantiteStock;

    // Constructeur pour JPA
    public ProduitFini() {
    }

    // Constructeur principal
    public ProduitFini(String code, String nom, String categorie, Production production, Double quantiteProduite) {
        this.code = validateCode(code);
        this.nom = validateNom(nom);
        this.categorie = validateCategorie(categorie);
        this.production = production;
        this.quantiteProduite = validateQuantiteProduite(quantiteProduite);
    }

    // Méthodes de validation (existantes, inchangées ici)
    private String validateCode(String code) {
        Objects.requireNonNull(code, "Le code ne peut pas être null");
        if (!code.matches("^PF-[A-Z]{3}-\\d{3}$")) {
            throw new IllegalArgumentException("Format de code invalide. Attendu: PF-XXX-999 (ex: PF-JUS-001)");
        }
        return code;
    }

    private String validateNom(String nom) {
        Objects.requireNonNull(nom, "Le nom ne peut pas être null");
        String trimmed = nom.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (trimmed.length() > 50) {
            throw new IllegalArgumentException("Le nom ne peut dépasser 50 caractères");
        }
        return trimmed;
    }

    private String validateCategorie(String categorie) {
        Objects.requireNonNull(categorie, "La catégorie ne peut pas être null");
        String trimmed = categorie.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("La catégorie ne peut pas être vide");
        }
        if (trimmed.length() > 20) {
            throw new IllegalArgumentException("La catégorie ne peut dépasser 20 caractères");
        }
        return trimmed;
    }

    private Double validateQuantiteProduite(Double quantiteProduite) {
        Objects.requireNonNull(quantiteProduite, "La quantité produite ne peut pas être null");
        if (quantiteProduite <= 0) {
            throw new IllegalArgumentException("La quantité produite doit être positive");
        }
        return quantiteProduite;
    }

    // Getters et Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = validateCode(code); }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = validateNom(nom); }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = validateCategorie(categorie); }

    public Production getProduction() { return production; }
    public void setProduction(Production production) { this.production = production; }

    public Double getQuantiteProduite() { return quantiteProduite; }
    public void setQuantiteProduite(Double quantiteProduite) { this.quantiteProduite = validateQuantiteProduite(quantiteProduite); }

    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public Double getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(Double quantiteStock) { this.quantiteStock = quantiteStock; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProduitFini)) return false;
        ProduitFini that = (ProduitFini) o;
        return Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return String.format("ProduitFini[code=%s, nom=%s, categorie=%s, quantiteProduite=%s]", code, nom, categorie, quantiteProduite);
    }
}