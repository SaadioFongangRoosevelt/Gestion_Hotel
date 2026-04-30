package dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import models.Reservation;

public class ReservationDAO {
    private static final String RESERVATION_FILE = "data/reservations.txt";
    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationDAO() {
        charger();
    }

    private void ensureDataFolder() {
        File file = new File(RESERVATION_FILE);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private void charger() {
        ensureDataFolder();
        java.nio.file.Path path = Paths.get(RESERVATION_FILE);
        if (!Files.exists(path)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                Reservation reservation = Reservation.depuisEnregistrement(line);
                if (reservation != null) {
                    reservations.add(reservation);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des réservations : " + e.getMessage());
        }
    }

    public List<Reservation> trouverParClient(String codeClient) {
        if (codeClient == null) {
            return new ArrayList<>();
        }
        String normalized = codeClient.trim().toUpperCase();
        return reservations.stream()
                .filter(reservation -> normalized.equals(reservation.getClientCode()))
                .collect(Collectors.toList());
    }

    public boolean ajouter(Reservation reservation) {
        if (reservation == null || reservation.getCode() == null) {
            return false;
        }
        reservations.add(reservation);
        enregistrer();
        return true;
    }

    private void enregistrer() {
        ensureDataFolder();
        java.nio.file.Path path = Paths.get(RESERVATION_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Reservation reservation : reservations) {
                writer.write(reservation.enregistrer());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement des réservations : " + e.getMessage());
        }
    }
}

