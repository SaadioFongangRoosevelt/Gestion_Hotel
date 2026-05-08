package util;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MatriculeGenerator {

    public static String generer(String suffixe, int compteur) {
        return suffixe + String.format("%03d", compteur);
    }

    public static String genererCodeClient(int compteur) {
        return "CLI" + String.format("%03d", compteur);
    }

    public static String genererCodeReservation(int compteur) {
        return "RES" + String.format("%03d", compteur);
    }

    public static String genererCodeChambre(int compteur) {
        return "CH" + String.format("%03d", compteur);
    }
}
