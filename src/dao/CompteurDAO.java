package dao;

import util.FileConfig;
import java.io.*;
import java.util.Properties;
public class CompteurDAO {
    private static final String KEY_GERANT     = "gerant";
    private static final String KEY_RECEP      = "receptionniste";
    private static final String KEY_CLIENT     = "client";
    private static final String KEY_RESERVATION= "reservation";
    private static final String KEY_CHAMBRE    = "chambre";

    private Properties props = new Properties();

    public CompteurDAO() {
        charger();
    }

    private void charger() {
        File f = new File(FileConfig.COMPTEURS_FILE);
        if (f.exists()) {
            try (InputStream in = new FileInputStream(f)) {
                props.load(in);
            } catch (IOException e) {
                // a ce niveau si le fichier est vide ou invalide, on repart de 0
            }
        }
    }

    private void sauvegarder() {
        new File(FileConfig.DATA_DIR).mkdirs();
        try (OutputStream out = new FileOutputStream(FileConfig.COMPTEURS_FILE)) {
            props.store(out, "Statistique de l'hotel");
        } catch (IOException e) {
            throw new RuntimeException("Erreur sauvegarde compteurs: " + e.getMessage());
        }
    }

    public int prochainGerant() {
        int val = Integer.parseInt(props.getProperty(KEY_GERANT, "0")) + 1;
        props.setProperty(KEY_GERANT, String.valueOf(val));
        sauvegarder();
        return val;
    }

    public int prochainReceptionniste() {
        int val = Integer.parseInt(props.getProperty(KEY_RECEP, "0")) + 1;
        props.setProperty(KEY_RECEP, String.valueOf(val));
        sauvegarder();
        return val;
    }

    public int prochainClient() {
        int val = Integer.parseInt(props.getProperty(KEY_CLIENT, "0")) + 1;
        props.setProperty(KEY_CLIENT, String.valueOf(val));
        sauvegarder();
        return val;
    }

    public int prochainReservation() {
        int val = Integer.parseInt(props.getProperty(KEY_RESERVATION, "0")) + 1;
        props.setProperty(KEY_RESERVATION, String.valueOf(val));
        sauvegarder();
        return val;
    }

    public int prochainChambre() {
        int val = Integer.parseInt(props.getProperty(KEY_CHAMBRE, "0")) + 1;
        props.setProperty(KEY_CHAMBRE, String.valueOf(val));
        sauvegarder();
        return val;
    }

    public boolean gerantExiste() {
        return Integer.parseInt(props.getProperty(KEY_GERANT, "0")) > 0;
    }
}
