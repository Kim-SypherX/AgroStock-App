package com.agrostock.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "inventaire")
public class Inventaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "responsable", nullable = false, length = 50)
    private String responsable;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 10)
    private StatutInventaire statut;

    @OneToMany(mappedBy = "inventaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneInventaire> lignes = new ArrayList<>();

    public enum StatutInventaire { BROUILLON, VALIDE, ANNULE }

    // Constructeur pour JPA
    public Inventaire() {
        this.statut = StatutInventaire.BROUILLON;
        this.date = LocalDate.now();
    }

    // Constructeur principal
    public Inventaire(LocalDate date, String responsable) {
        this();
        setDate(date);
        setResponsable(responsable);
    }

    // Constructeur complet
    public Inventaire(Integer id, LocalDate date, String responsable, StatutInventaire statut) {
        this(date, responsable);
        this.id = id;
        setStatut(statut);
    }

    // Validation du responsable
    private String validateResponsable(String responsable) {
        Objects.requireNonNull(responsable, "Le responsable ne peut pas être null");
        String trimmed = responsable.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Le responsable ne peut pas être vide");
        }
        if (trimmed.length() > 50) {
            throw new IllegalArgumentException("Le nom du responsable ne peut dépasser 50 caractères");
        }
        return trimmed;
    }

    // Méthode d'ajout de ligne avec validation
    public void ajouterLigne(LigneInventaire ligne) {
        if (statut != StatutInventaire.BROUILLON) {
            throw new IllegalStateException("Seuls les inventaires brouillons peuvent être modifiés");
        }
        Objects.requireNonNull(ligne, "La ligne ne peut pas être null");
        ligne.setInventaire(this);
        this.lignes.add(ligne);
    }

    // Validation de l'inventaire
    public void valider() {
        if (lignes.isEmpty()) {
            throw new IllegalStateException("Impossible de valider un inventaire vide");
        }
        this.statut = StatutInventaire.VALIDE;
    }

    // Annulation de l'inventaire
    public void annuler() {
        this.statut = StatutInventaire.ANNULE;
    }

    // Vue immuable des lignes
    public List<LigneInventaire> getLignes() {
        return Collections.unmodifiableList(lignes);
    }

    // Getters et Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = Objects.requireNonNull(date, "La date ne peut pas être null");
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = validateResponsable(responsable);
    }

    public StatutInventaire getStatut() {
        return statut;
    }

    public void setStatut(StatutInventaire statut) {
        this.statut = Objects.requireNonNull(statut, "Le statut ne peut pas être null");
    }

    // equals/hashCode/toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventaire)) return false;
        Inventaire that = (Inventaire) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "Inventaire[id=%d, date=%s, responsable='%s', statut=%s, nbLignes=%d]",
                id, date, responsable, statut, lignes.size()
        );
    }
}