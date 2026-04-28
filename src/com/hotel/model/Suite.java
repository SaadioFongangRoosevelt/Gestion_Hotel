package com.hotel.model;

/**
 * Chambre de catégorie Suite.
 */
public class Suite extends Chambre {
    private static final double COEFF = 2.5;

    public Suite(String id, String numero, int etage, int capacite, TypeChambre typeChambre) {
        super(id, numero, etage, capacite, typeChambre);
    }

    @Override
    public double getPrixNuit() {
        return typeChambre.getPrixBase() * COEFF;
    }
}
