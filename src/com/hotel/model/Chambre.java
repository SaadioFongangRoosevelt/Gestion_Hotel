package com.hotel.model;

import java.time.LocalDate;

/**
 * Classe abstraite représentant une chambre de l'hôtel.
 */
public abstract class Chambre {
    protected String id;
    protected String numero;
    protected int etage;
    protected int capacite;
    protected EtatChambreEnum etat;
    protected TypeChambre typeChambre;

    public Chambre(String id, String numero, int etage, int capacite, TypeChambre typeChambre) {
        this.id = id;
        this.numero = numero;
        this.etage = etage;
        this.capacite = capacite;
        this.typeChambre = typeChambre;
        this.etat = EtatChambreEnum.DISPONIBLE; // Par défaut
    }

    public void changerEtat(EtatChambreEnum nouvelEtat) {
        this.etat = nouvelEtat;
    }

    /**
     * Calcule le prix de la nuit en fonction du type et du coefficient de la sous-classe.
     */
    public abstract double getPrixNuit();

    /**
     * Vérifie la disponibilité de la chambre pour une période donnée.
     * Note: Dans une version complète, cette méthode consulterait la liste des réservations.
     */
    public boolean estDispo(LocalDate debut, LocalDate fin) {
        // Pour l'instant, on se base sur l'état actuel.
        // Si l'état est DISPONIBLE, elle est considérée libre.
        return this.etat == EtatChambreEnum.DISPONIBLE;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getNumero() { return numero; }
    public int getEtage() { return etage; }
    public int getCapacite() { return capacite; }
    public EtatChambreEnum getEtat() { return etat; }
    public TypeChambre getTypeChambre() { return typeChambre; }

    @Override
    public String toString() {
        return "Chambre " + numero + " [" + typeChambre.getLibelle() + "] - Etage " + etage + " - Etat: " + etat;
    }
}
