package com.hotel.model;

/**
 * Enumération représentant les différents états possibles d'une chambre.
 */
public enum EtatChambreEnum {
    DISPONIBLE,
    RESERVEE,
    OCCUPEE,
    EN_NETTOYAGE,
    HORS_SERVICE;

    @Override
    public String toString() {
        return name();
    }
}
