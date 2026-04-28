package com.hotel.dao;

import com.hotel.model.*;
import com.hotel.util.IdGenerator;
import java.io.*;
import java.util.*;

/**
 * Implémentation du DAO pour les chambres utilisant des fichiers texte.
 */
public class ChambreDaoTxt implements IDao<Chambre> {
    private static final String FILE_NAME = "chambres.txt";
    private Map<String, Chambre> chambres = new HashMap<>();

    public ChambreDaoTxt() {
        chargerDonnees();
    }

    @Override
    public void sauvegarder(Chambre chambre) {
        chambres.put(chambre.getId(), chambre);
        enregistrerDonnees();
    }

    @Override
    public Optional<Chambre> trouverParId(String id) {
        return Optional.ofNullable(chambres.get(id));
    }

    @Override
    public List<Chambre> trouverTous() {
        return new ArrayList<>(chambres.values());
    }

    @Override
    public void modifier(Chambre chambre) {
        if (chambres.containsKey(chambre.getId())) {
            chambres.put(chambre.getId(), chambre);
            enregistrerDonnees();
        }
    }

    @Override
    public void supprimer(String id) {
        if (chambres.remove(id) != null) {
            enregistrerDonnees();
        }
    }

    @Override
    public String genererProchainId() {
        int nextSeq = chambres.size() + 1;
        return IdGenerator.generer(IdGenerator.PREFIX_CHAMBRE, nextSeq);
    }

    private void chargerDonnees() {
        // Simulation de lecture de fichier
        // Dans une version réelle, on lirait chaque ligne et reconstruirait les objets.
    }

    private void enregistrerDonnees() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Chambre c : chambres.values()) {
                writer.println(c.getId() + ";" + c.getNumero() + ";" + c.getEtage() + ";" + 
                               c.getCapacite() + ";" + c.getEtat() + ";" + 
                               c.getTypeChambre().getLibelle() + ";" + c.getClass().getSimpleName());
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement des chambres: " + e.getMessage());
        }
    }
}
