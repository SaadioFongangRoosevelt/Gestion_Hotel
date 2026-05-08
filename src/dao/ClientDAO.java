package dao;

import models.Client;
import util.FileConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void sauvegarder(Client client) throws IOException {
        new File(FileConfig.DATA_DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.CLIENTS_FILE, true))) {
            bw.write(client.toString());
            bw.newLine();
        }
    }

    public List<Client> lireTous() throws IOException {
        List<Client> liste = new ArrayList<>();
        File f = new File(FileConfig.CLIENTS_FILE);
        if (!f.exists()) return liste;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.isBlank()) continue;
                String[] parts = ligne.split("\t");
                liste.add(new Client(parts[0], parts[1], parts[2], parts[3]));
            }
        }
        return liste;
    }

    public Client trouverParCode(String code) throws IOException {
        for (Client c : lireTous()) {
            if (c.getCodeClient().equals(code)) return c;
        }
        return null;
    }

    public boolean supprimer(String codeClient) throws IOException {
        List<Client> liste = lireTous();
        boolean trouve = liste.removeIf(c -> c.getCodeClient().equals(codeClient));
        if (trouve) ecrireTous(liste);
        return trouve;
    }

    public boolean mettreAJour(Client modifie) throws IOException {
        List<Client> liste = lireTous();
        boolean trouve = false;
        for (int i = 0; i < liste.size(); i++) {
            if (liste.get(i).getCodeClient().equals(modifie.getCodeClient())) {
                liste.set(i, modifie);
                trouve = true;
                break;
            }
        }
        if (trouve) ecrireTous(liste);
        return trouve;
    }

    private void ecrireTous(List<Client> liste) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.CLIENTS_FILE, false))) {
            for (Client c : liste) {
                bw.write(c.toString());
                bw.newLine();
            }
        }
    }
}
