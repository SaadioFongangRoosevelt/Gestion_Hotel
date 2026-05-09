package com.hotel.model;

/**
 * Chambre de catégorie Standard.
 */
public class Standard extends Chambre {
    private static final double COEFF = 1.0;

    public Standard(String id, String numero, int etage, int capacite, TypeChambre typeChambre) {
        super(id, numero, etage, capacite, typeChambre);
    }

    @Override
    public double getPrixNuit() {
        return typeChambre.getPrixBase() * COEFF;
    }
}
