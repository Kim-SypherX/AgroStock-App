package com.agrostock.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;

    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductionDetail> details;

    @Column(nullable = false)
    private LocalDateTime date;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public List<ProductionDetail> getDetails() { return details; }
    public void setDetails(List<ProductionDetail> details) { this.details = details; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}