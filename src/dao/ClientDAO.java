package dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import models.Client;

public class ClientDAO {
    private static final String CLIENT_FILE = "data/clients.txt";
    private final Map<String, Client> clients = new LinkedHashMap<>();

    public ClientDAO() {
        charger();
    }

    private void ensureDataFolder() {
        File file = new File(CLIENT_FILE);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private void charger() {
        ensureDataFolder();
        java.nio.file.Path path = Paths.get(CLIENT_FILE);
        if (!Files.exists(path)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                if (line == null || line.trim().isEmpty()) {
                    continue;
                }
                Client client = Client.depuisEnregistrement(line);
                if (client != null) {
                    clients.put(client.getCode(), client);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des clients : " + e.getMessage());
        }
    }

    public List<Client> chercherTous() {
        return new ArrayList<>(clients.values());
    }

    public Optional<Client> chercherParCode(String code) {
        if (code == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(clients.get(code.trim().toUpperCase()));
    }

    public List<Client> rechercher(String terme) {
        if (terme == null || terme.trim().isEmpty()) {
            return chercherTous();
        }
        String lower = terme.trim().toLowerCase();
        return clients.values().stream()
                .filter(client -> client.getCode().toLowerCase().contains(lower)
                        || client.getNom().toLowerCase().contains(lower)
                        || client.getAdresse().toLowerCase().contains(lower)
                        || client.getTelephone().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }

    public Client ajouter(Client client) {
        String code = genererProchainCode();
        client.setCode(code);
        clients.put(code, client);
        enregistrer();
        return client;
    }

    public boolean modifier(Client client) {
        if (client == null || client.getCode() == null || !clients.containsKey(client.getCode())) {
            return false;
        }
        clients.put(client.getCode(), client);
        enregistrer();
        return true;
    }

    public boolean supprimer(String code) {
        if (code == null) {
            return false;
        }
        String normalized = code.trim().toUpperCase();
        if (clients.remove(normalized) != null) {
            enregistrer();
            return true;
        }
        return false;
    }

    private String genererProchainCode() {
        int max = clients.keySet().stream()
                .map(key -> {
                    try {
                        if (key.startsWith("CLT")) {
                            return Integer.parseInt(key.substring(3));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                    return 0;
                })
                .max(Comparator.naturalOrder())
                .orElse(0);
        return String.format("CLT%04d", max + 1);
    }

    private void enregistrer() {
        ensureDataFolder();
        java.nio.file.Path path = Paths.get(CLIENT_FILE);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
            for (Client client : clients.values()) {
                writer.write(client.enregistrer());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de l'enregistrement des clients : " + e.getMessage());
        }
    }
}

