package com.agrostock.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "fournisseur")
public class Fournisseur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "contact", length = 100)
    private String contact;

    @Column(name = "solde_credit", nullable = false)
    private double soldeCredit;

    // Constructeur pour JPA
    public Fournisseur() {
        this.soldeCredit = 0.0;
    }

    // Constructeur principal
    public Fournisseur(String nom, String contact, double soldeCredit) {
        this.nom = validateNom(nom);
        this.contact = contact;
        this.soldeCredit = validateSoldeCredit(soldeCredit);
    }

    // Méthodes de validation
    private String validateNom(String nom) {
        Objects.requireNonNull(nom, "Le nom ne peut pas être null");
        String trimmed = nom.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (trimmed.length() > 100) {
            throw new IllegalArgumentException("Le nom ne peut dépasser 100 caractères");
        }
        return trimmed;
    }

    private double validateSoldeCredit(double soldeCredit) {
        return soldeCredit;
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = validateNom(nom);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public double getSoldeCredit() {
        return soldeCredit;
    }

    public void setSoldeCredit(double soldeCredit) {
        this.soldeCredit = validateSoldeCredit(soldeCredit);
    }

    // equals/hashCode/toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fournisseur)) return false;
        Fournisseur that = (Fournisseur) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Fournisseur[id=%d, nom=%s, contact=%s, soldeCredit=%.2f]",
                id, nom, contact, soldeCredit
        );
    }
}