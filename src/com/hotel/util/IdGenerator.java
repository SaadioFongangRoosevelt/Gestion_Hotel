package com.hotel.util;

/**
 * Utilitaire pour générer des IDs uniques avec préfixes.
 */
public class IdGenerator {
    
    public static final String PREFIX_CHAMBRE = "CHB";
    public static final String PREFIX_RECEPTIONNISTE = "RCP";
    public static final String PREFIX_CLIENT = "CLT";
    public static final String PREFIX_RESERVATION = "RSV";
    public static final String PREFIX_FACTURE = "FAC";
    public static final String PREFIX_PAIEMENT = "PAY";

    /**
     * Génère un ID formaté (ex: CHB001).
     * @param prefix Le préfixe de l'ID.
     * @param sequence Le numéro de séquence.
     * @return L'ID formaté.
     */
    public static String generer(String prefix, int sequence) {
        return prefix + String.format("%03d", sequence);
    }
}
