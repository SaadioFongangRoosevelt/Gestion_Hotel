package dao;

import java.io.*;
import java.util.Properties;
import util.FileConfig;

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
                // fichier vide ou invalide, on repart de 0
            }
        }
    }

}
