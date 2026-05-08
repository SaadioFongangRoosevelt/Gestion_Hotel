package dao;

import models.Reservation;
import util.FileConfig;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public void sauvegarder(Reservation reservation) throws IOException {
        new File(FileConfig.DATA_DIR).mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.RESERVATIONS_FILE, true))) {
            bw.write(reservation.toString());
            bw.newLine();
        }
    }

    public List<Reservation> lireTous() throws IOException {
        List<Reservation> liste = new ArrayList<>();
        File f = new File(FileConfig.RESERVATIONS_FILE);
        if (!f.exists()) return liste;

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                if (ligne.isBlank()) continue;
                String[] parts = ligne.split("\t");
                liste.add(new Reservation(
                        parts[0], parts[1], parts[2], parts[3],
                        Double.parseDouble(parts[4]),
                        LocalDate.parse(parts[5]),
                        Integer.parseInt(parts[6])
                ));
            }
        }
        return liste;
    }

    public Reservation trouverParCodeClient(String codeClient) throws IOException {
        for (Reservation r : lireTous()) {
            if (r.getCodeClient().equals(codeClient)) return r;
        }
        return null;
    }

    public boolean supprimerParCodeClient(String codeClient) throws IOException {
        List<Reservation> liste = lireTous();
        boolean trouve = liste.removeIf(r -> r.getCodeClient().equals(codeClient));
        if (trouve) ecrireTous(liste);
        return trouve;
    }

    private void ecrireTous(List<Reservation> liste) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.RESERVATIONS_FILE, false))) {
            for (Reservation r : liste) {
                bw.write(r.toString());
                bw.newLine();
            }
        }
    }
}
