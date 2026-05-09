package dao;

import models.Chambre;
import util.FileConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChambreDAO {

    public void sauvegarder(Chambre chambre) throws IOException {
        new File(FileConfig.DATA_DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.CHAMBRES_FILE, true))) {
            bw.write(chambre.toString());
            bw.newLine();
        }
    }

    public List<Chambre> lireTous() throws IOException {
        List<Chambre> liste = new ArrayList<>();
        File f = new File(FileConfig.CHAMBRES_FILE);
        if (!f.exists()) return liste;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.isBlank()) continue;
                String[] parts = ligne.split("\t");
                Chambre.Standing standing = Chambre.Standing.valueOf(parts[1]);
                Chambre.Statut statut = Chambre.Statut.valueOf(parts[2]);
                liste.add(new Chambre(parts[0], standing, statut, Double.parseDouble(parts[3])));
            }
        }
        return liste;
    }

    public Chambre trouverParCode(String code) throws IOException {
        for (Chambre c : lireTous()) {
            if (c.getCodeChambre().equals(code)) return c;
        }
        return null;
    }

    public List<Chambre> lireLibres() throws IOException {
        List<Chambre> libres = new ArrayList<>();
        for (Chambre c : lireTous()) {
            if (c.estLibre()) libres.add(c);
        }
        return libres;
    }

    public boolean mettreAJour(Chambre modifiee) throws IOException {
        List<Chambre> liste = lireTous();
        boolean trouve = false;
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getCodeChambre().equals(modifiee.getCodeChambre())) {
                liste.set(i, modifiee);
                trouve = true;
                break;
            }
        }
        if (trouve) ecrireTous(liste);
        return trouve;
    }

    public boolean supprimer(String codeChambre) throws IOException {
        List<Chambre> liste = lireTous();
        boolean trouve = liste.removeIf(c -> c.getCodeChambre().equals(codeChambre));
        if (trouve) ecrireTous(liste);
        return trouve;
    }

    private void ecrireTous(List<Chambre> liste) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.CHAMBRES_FILE, false))) {
            for (Chambre c : liste) {
                bw.write(c.toString());
                bw.newLine();
            }
        }
    }
}
