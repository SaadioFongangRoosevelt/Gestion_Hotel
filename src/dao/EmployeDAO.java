package dao;

import models.Employe;
import models.Gerant;
import models.Receptionniste;
import util.FileConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    public void sauvegarder(Employe employe) throws IOException {
        new File(FileConfig.DATA_DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.EMPLOYES_FILE, true))) {
            bw.write(employe.toString());
            bw.newLine();
        }
    }

    public List<Employe> lireTous() throws IOException {
        List<Employe> liste = new ArrayList<>();
        File f = new File(FileConfig.EMPLOYES_FILE);
        if (!f.exists()) return liste;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.isBlank()) continue;
                String[] parts = ligne.split("\t");
                String role = parts[6];
                if ("GERANT".equals(role)) {
                    liste.add(new Gerant(parts[0], parts[1], parts[2], parts[3],
                            Double.parseDouble(parts[4]), parts[5]));
                } else if ("RECEPTIONNISTE".equals(role)) {
                    int nbClients = parts.length > 7 ? Integer.parseInt(parts[7]) : 0;
                    liste.add(new Receptionniste(parts[0], parts[1], parts[2], parts[3],
                            Double.parseDouble(parts[4]), parts[5], nbClients));
                }
            }
        }
        return liste;
    }

    public Employe trouverParMatricule(String matricule) throws IOException {
        for (Employe e : lireTous()) {
            if (e.getMatricule().equals(matricule)) return e;
        }
        return null;
    }

    public List<Receptionniste> lireReceptionnistes() throws IOException {
        List<Receptionniste> liste = new ArrayList<>();
        for (Employe e : lireTous()) {
            if (e instanceof Receptionniste) liste.add((Receptionniste) e);
        }
        return liste;
    }

    public Gerant lireGerant() throws IOException {
        for (Employe e : lireTous()) {
            if (e instanceof Gerant) return (Gerant) e;
        }
        return null;
    }

    public boolean mettreAJour(Employe modifie) throws IOException {
        List<Employe> liste = lireTous();
        boolean trouve = false;
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getMatricule().equals(modifie.getMatricule())) {
                liste.set(i, modifie);
                trouve = true;
                break;
            }
        }
        if (trouve) ecrireTous(liste);
        return trouve;
    }

    private void ecrireTous(List<Employe> liste) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.EMPLOYES_FILE, false))) {
            for (Employe e : liste) {
                bw.write(e.toString());
                bw.newLine();
            }
        }
    }
}
