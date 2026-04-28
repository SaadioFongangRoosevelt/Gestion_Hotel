package com.hotel;

import com.hotel.dao.ChambreDaoTxt;
import com.hotel.model.*;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Système de Gestion d'Hôtel - Module Chambre ===");

        // 1. Initialisation des types de chambres
        TypeChambre simple = new TypeChambre("TCH001", "Simple", 50.0, "Une petite chambre confortable");
        TypeChambre doubleType = new TypeChambre("TCH002", "Double", 80.0, "Idéal pour les couples");

        // 2. Gestion des chambres (DAO)
        ChambreDaoTxt chambreDao = new ChambreDaoTxt();

        // Création de quelques chambres
        Chambre ch1 = new Standard(chambreDao.genererProchainId(), "101", 1, 1, simple);
        chambreDao.sauvegarder(ch1);

        Chambre ch2 = new VIP(chambreDao.genererProchainId(), "201", 2, 2, doubleType);
        chambreDao.sauvegarder(ch2);

        Chambre ch3 = new Suite(chambreDao.genererProchainId(), "301", 3, 4, doubleType);
        chambreDao.sauvegarder(ch3);

        // 3. Affichage de la liste des chambres (Statut disponibilité)
        System.out.println("\n--- Liste des chambres disponibles ---");
        List<Chambre> toutesLesChambres = chambreDao.trouverTous();
        for (Chambre c : toutesLesChambres) {
            System.out.println(c + " | Prix/Nuit: " + c.getPrixNuit() + "€");
        }

        // 4. Affectation (Changement d'état)
        System.out.println("\n--- Affectation de la chambre 201 ---");
        ch2.changerEtat(EtatChambreEnum.OCCUPEE);
        chambreDao.modifier(ch2);
        System.out.println("Nouvel état de la " + ch2.getNumero() + " : " + ch2.getEtat());

        // 5. Vérification disponibilité pour une période
        System.out.println("\n--- Vérification disponibilité ---");
        LocalDate debut = LocalDate.now();
        LocalDate fin = debut.plusDays(3);
        System.out.println("Chambre 101 dispo ? " + ch1.estDispo(debut, fin));
        System.out.println("Chambre 201 dispo ? " + ch2.estDispo(debut, fin));

        System.out.println("\nFin du programme.");
    }
}
