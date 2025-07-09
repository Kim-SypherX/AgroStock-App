package com.agrostock.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ligne_inventaire")
public class LigneInventaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventaire_id", nullable = false)
    private Inventaire inventaire;

    @Column(name = "produit_code", nullable = false, length = 10)
    private String produitCode;

    @Column(name = "quantite_reelle", nullable = false)
    private double quantiteReelle;

    @Column(name = "ecart")
    private Double ecart;

    // Constructeur pour JPA
    public LigneInventaire() {
    }

    // Constructeur principal
    public LigneInventaire(Inventaire inventaire, String produitCode, double quantiteReelle) {
        this.inventaire = Objects.requireNonNull(inventaire, "L'inventaire ne peut pas être null");
        this.produitCode = validateProduitCode(produitCode);
        this.quantiteReelle = validateQuantite(quantiteReelle);
    }

    // Validation du code produit
    private String validateProduitCode(String code) {
        Objects.requireNonNull(code, "Le code produit ne peut pas être null");
        if (!code.matches("^MP-[A-Z]{3}-\\d{3}$")) {
            throw new IllegalArgumentException("Format de code invalide. Attendu: MP-XXX-999 (ex: MP-MAN-001)");
        }
        return code;
    }

    // Validation de la quantité
    private double validateQuantite(double quantite) {
        if (quantite < 0) {
            throw new IllegalArgumentException("La quantité réelle ne peut pas être négative");
        }
        return quantite;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Inventaire getInventaire() {
        return inventaire;
    }

    public void setInventaire(Inventaire inventaire) {
        this.inventaire = Objects.requireNonNull(inventaire, "L'inventaire ne peut pas être null");
    }

    public String getProduitCode() {
        return produitCode;
    }

    public void setProduitCode(String produitCode) {
        this.produitCode = validateProduitCode(produitCode);
    }

    public double getQuantiteReelle() {
        return quantiteReelle;
    }

    public void setQuantiteReelle(double quantiteReelle) {
        this.quantiteReelle = validateQuantite(quantiteReelle);
    }

    public Double getEcart() {
        return ecart;
    }

    public void setEcart(Double ecart) {
        this.ecart = ecart;
    }

    // equals/hashCode/toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LigneInventaire)) return false;
        LigneInventaire that = (LigneInventaire) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "LigneInventaire[id=%d, produitCode=%s, quantiteReelle=%.2f, ecart=%s]",
                id, produitCode, quantiteReelle, ecart != null ? String.format("%.2f", ecart) : "non calculé"
        );
    }
}