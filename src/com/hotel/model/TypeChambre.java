package com.hotel.model;

/**
 * Classe représentant une catégorie de chambre (ex: Simple, Double, Luxe).
 */
public class TypeChambre {
    private String id;
    private String libelle;
    private double prixBase;
    private String description;

    public TypeChambre(String id, String libelle, double prixBase, String description) {
        this.id = id;
        this.libelle = libelle;
        this.prixBase = prixBase;
        this.description = description;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public double getPrixBase() { return prixBase; }
    public void setPrixBase(double prixBase) { this.prixBase = prixBase; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return libelle + " (" + prixBase + "€)";
    }
}
