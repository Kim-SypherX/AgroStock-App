package com.agrostock.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "operation_credit")
public class OperationCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private TypeOperation type;

    @Column(name = "montant", nullable = false)
    private double montant;

    @Column(name = "reference", length = 20)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 10)
    private StatutOperation statut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "operationCredit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MouvementStock> mouvements = new ArrayList<>();

    public enum TypeOperation { ACHAT, VENTE }
    public enum StatutOperation { NON_PAYE, PAYE, PARTIEL }

    // Constructeur pour JPA
    public OperationCredit() {
        this.statut = StatutOperation.NON_PAYE;
        this.date = LocalDate.now();
    }

    // Constructeur principal
    public OperationCredit(LocalDate date, TypeOperation type, double montant, String reference, StatutOperation statut, Fournisseur fournisseur, Client client) {
        this();
        setDate(date);
        this.type = Objects.requireNonNull(type, "Le type d'opération ne peut pas être null");
        this.montant = validateMontant(montant);
        this.reference = reference != null ? reference.trim() : null;
        this.statut = Objects.requireNonNull(statut, "Le statut ne peut pas être null");
        this.fournisseur = fournisseur;
        this.client = client;
    }

    // Validation du montant
    private double validateMontant(double montant) {
        if (montant < 0) {
            throw new IllegalArgumentException("Le montant ne peut pas être négatif");
        }
        return montant;
    }

    // Méthode d'ajout de mouvement
    public void ajouterMouvement(MouvementStock mouvement) {
        Objects.requireNonNull(mouvement, "Le mouvement ne peut pas être null");
        mouvement.setOperationCredit(this);
        this.mouvements.add(mouvement);
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

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = Objects.requireNonNull(type, "Le type d'opération ne peut pas être null");
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = validateMontant(montant);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference != null ? reference.trim() : null;
    }

    public StatutOperation getStatut() {
        return statut;
    }

    public void setStatut(StatutOperation statut) {
        this.statut = Objects.requireNonNull(statut, "Le statut ne peut pas être null");
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<MouvementStock> getMouvements() {
        return new ArrayList<>(mouvements); // Retourne une copie pour éviter les modifications directes
    }

    // equals/hashCode/toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationCredit)) return false;
        OperationCredit that = (OperationCredit) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "OperationCredit[id=%d, date=%s, type=%s, montant=%.2f, reference=%s, statut=%s, fournisseur=%s, client=%s]",
                id, date, type, montant, reference, statut, fournisseur != null ? fournisseur.getNom() : null, client != null ? client.getNom() : null
        );
    }
}