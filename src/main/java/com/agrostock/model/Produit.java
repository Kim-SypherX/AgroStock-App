package com.agrostock.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "produit")
public class Produit {

    @Id
    @Column(name = "code", length = 10, nullable = false)
    private String code;

    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @Column(name = "type", length = 20, nullable = false)
    private String type;

    @Column(name = "unite", length = 5, nullable = false)
    private String unite;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "quantite_stock", nullable = false)
    private Double quantiteStock;

    // Constructeur par défaut (requis par JPA)
    public Produit() {
    }

    // Constructeur principal
    public Produit(String code, String nom, String type, String unite, Double prixUnitaire, Double quantiteStock) {
        this.code = code;
        this.nom = nom;
        this.type = type;
        this.unite = unite;
        this.prixUnitaire = prixUnitaire != null ? prixUnitaire : 0.0;
        this.quantiteStock = quantiteStock != null ? quantiteStock : 0.0;
    }

    // Getters et Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getUnite() { return unite; }
    public void setUnite(String unite) { this.unite = unite; }
    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = prixUnitaire != null ? prixUnitaire : 0.0; }
    public Double getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(Double quantiteStock) { this.quantiteStock = quantiteStock != null ? quantiteStock : 0.0; }

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Produit)) return false; Produit that = (Produit) o; return Objects.equals(code, that.code); }
    @Override
    public int hashCode() { return Objects.hash(code); }
    @Override
    public String toString() { return String.format("Produit[code=%s, nom=%s, type=%s, unite=%s, prixUnitaire=%.2f, quantiteStock=%.2f]", code, nom, type, unite, prixUnitaire, quantiteStock); }
}