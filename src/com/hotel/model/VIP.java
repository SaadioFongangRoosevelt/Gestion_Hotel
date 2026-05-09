package com.hotel.model;

/**
 * Chambre de catégorie VIP.
 */
public class VIP extends Chambre {
    private static final double COEFF = 1.5;

    public VIP(String id, String numero, int etage, int capacite, TypeChambre typeChambre) {
        super(id, numero, etage, capacite, typeChambre);
    }

    @Override
    public double getPrixNuit() {
        return typeChambre.getPrixBase() * COEFF;
    }
}
