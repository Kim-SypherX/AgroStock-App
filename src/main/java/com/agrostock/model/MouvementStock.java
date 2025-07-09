package com.agrostock.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "mouvement_stock")
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private TypeMouvement type;

    @Column(name = "produit_code", nullable = false, length = 10)
    private String produitCode;

    @Column(name = "quantite", nullable = false)
    private Double quantite;

    @Column(name = "prix_unitaire", nullable = false)
    private Double prixUnitaire;

    @Column(name = "motif", length = 100)
    private String motif;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fournisseur_id")
    private Fournisseur fournisseur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operation_credit_id")
    private OperationCredit operationCredit;

    @Enumerated(EnumType.STRING)
    @Column(name = "produit_type", nullable = false)
    private ProduitType produitType;

    public enum TypeMouvement { ENTREE, SORTIE }
    public enum ProduitType { MATIERE_PREMIERE, PRODUIT_FINI, PRODUCTION } // Ajout de PRODUCTION

    // Constructeur pour JPA
    public MouvementStock() {
    }

    // Constructeur principal
    public MouvementStock(Integer id, LocalDateTime date, TypeMouvement type, String produitCode, Double quantite, Double prixUnitaire, String motif, Fournisseur fournisseur, Client client, OperationCredit operationCredit, ProduitType produitType) {
        this.id = id;
        this.date = date != null ? date : LocalDateTime.now();
        this.type = Objects.requireNonNull(type, "Le type de mouvement ne peut pas être null");
        this.produitCode = validateProduitCode(produitCode);
        this.quantite = validateQuantite(quantite);
        this.prixUnitaire = validatePrixUnitaire(prixUnitaire);
        this.motif = motif != null ? motif.trim() : null;
        this.fournisseur = fournisseur;
        this.client = client;
        this.operationCredit = operationCredit;
        this.produitType = Objects.requireNonNull(produitType, "Le type de produit ne peut pas être null");
    }

    private String validateProduitCode(String code) {
        Objects.requireNonNull(code, "Le code produit ne peut pas être null");
        // Ajuster la validation pour accepter les codes de MatierePremiere (ex. MAG001) et Production (ex. nom)
        if (!code.matches("^[A-Z]{3}\\d{3}$|^[A-Za-z\\s]+$")) {
            throw new IllegalArgumentException("Format de code invalide. Attendu: XXX999 (ex: MAG001) ou nom libre pour Production");
        }
        return code;
    }

    private Double validateQuantite(Double quantite) {
        if (quantite == null || quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        return quantite;
    }

    private Double validatePrixUnitaire(Double prixUnitaire) {
        if (prixUnitaire == null || prixUnitaire < 0) {
            throw new IllegalArgumentException("Le prix unitaire doit être positif ou nul");
        }
        return prixUnitaire;
    }

    // Getters et Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date != null ? date : LocalDateTime.now(); }
    public TypeMouvement getType() { return type; }
    public void setType(TypeMouvement type) { this.type = Objects.requireNonNull(type, "Le type de mouvement ne peut pas être null"); }
    public String getProduitCode() { return produitCode; }
    public void setProduitCode(String produitCode) { this.produitCode = validateProduitCode(produitCode); }
    public Double getQuantite() { return quantite; }
    public void setQuantite(Double quantite) { this.quantite = validateQuantite(quantite); }
    public Double getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(Double prixUnitaire) { this.prixUnitaire = validatePrixUnitaire(prixUnitaire); }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif != null ? motif.trim() : null; }
    public Fournisseur getFournisseur() { return fournisseur; }
    public void setFournisseur(Fournisseur fournisseur) { this.fournisseur = fournisseur; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public OperationCredit getOperationCredit() { return operationCredit; }
    public void setOperationCredit(OperationCredit operationCredit) { this.operationCredit = operationCredit; }
    public ProduitType getProduitType() { return produitType; }
    public void setProduitType(ProduitType produitType) { this.produitType = Objects.requireNonNull(produitType, "Le type de produit ne peut pas être null"); }

    @Override
    public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof MouvementStock)) return false; MouvementStock that = (MouvementStock) o; return Objects.equals(id, that.id); }
    @Override
    public int hashCode() { return Objects.hash(id); }
    @Override
    public String toString() { return String.format("MouvementStock[id=%s, date=%s, type=%s, produitCode=%s, quantite=%.2f, prixUnitaire=%.2f, motif=%s, operationCredit=%s]", id, date, type, produitCode, quantite, prixUnitaire, motif, operationCredit != null ? operationCredit.getId() : null); }
}