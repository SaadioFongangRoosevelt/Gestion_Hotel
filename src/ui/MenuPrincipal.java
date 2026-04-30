package ui;

import java.util.List;
import java.util.Scanner;
import models.Client;
import models.Reservation;
import service.ClientService;

public class MenuPrincipal {
    private final ClientService clientService = new ClientService();
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new MenuPrincipal().run();
    }

    private void run() {
        while (true) {
            afficherMenuPrincipal();
            int choix = lireEntier("Choisissez une option : ");
            switch (choix) {
                case 1 -> gererClients();
                case 0 -> {
                    System.out.println("Au revoir !");
                    return;
                }
                default -> System.out.println("Option invalide. Réessayez.");
            }
        }
    }

    private void afficherMenuPrincipal() {
        System.out.println("\n=== Gestion de l'hôtel : Module Clients ===");
        System.out.println("1. Gestion des clients");
        System.out.println("0. Quitter");
    }

    private void gererClients() {
        while (true) {
            afficherMenuClients();
            int choix = lireEntier("Choisissez une action client : ");
            switch (choix) {
                case 1 -> ajouterClient();
                case 2 -> modifierClient();
                case 3 -> supprimerClient();
                case 4 -> afficherClients();
                case 5 -> rechercherClient();
                case 6 -> afficherHistoriqueReservation();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Option invalide. Réessayez.");
            }
        }
    }

    private void afficherMenuClients() {
        System.out.println("\n--- Clients ---");
        System.out.println("1. Ajouter un client");
        System.out.println("2. Modifier un client");
        System.out.println("3. Supprimer un client");
        System.out.println("4. Afficher tous les clients");
        System.out.println("5. Rechercher un client");
        System.out.println("6. Historique de réservation d'un client");
        System.out.println("0. Retour");
    }

    private void ajouterClient() {
        System.out.println("\n--- Ajouter un client ---");
        String nom = lireTexte("Nom : ");
        String adresse = lireTexte("Adresse : ");
        String telephone = lireTexte("Téléphone : ");
        try {
            Client client = clientService.creerClient(nom, adresse, telephone);
            System.out.println("Client créé avec succès : " + client.getCode());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private void modifierClient() {
        System.out.println("\n--- Modifier un client ---");
        String code = lireTexte("Code client (CLTxxxx) : ");
        String nom = lireTexte("Nouveau nom (laisser vide pour conserver) : ");
        String adresse = lireTexte("Nouvelle adresse (laisser vide pour conserver) : ");
        String telephone = lireTexte("Nouveau téléphone (laisser vide pour conserver) : ");
        boolean modifie = clientService.modifierClient(code, nom.isBlank() ? null : nom, adresse.isBlank() ? null : adresse, telephone.isBlank() ? null : telephone);
        if (modifie) {
            System.out.println("Client modifié avec succès.");
        } else {
            System.out.println("Client introuvable ou modification impossible.");
        }
    }

    private void supprimerClient() {
        System.out.println("\n--- Supprimer un client ---");
        String code = lireTexte("Code client (CLTxxxx) : ");
        if (clientService.supprimerClient(code)) {
            System.out.println("Client supprimé avec succès.");
        } else {
            System.out.println("Client introuvable ou suppression impossible.");
        }
    }

    private void afficherClients() {
        System.out.println("\n--- Liste des clients ---");
        List<Client> clients = clientService.listerClients();
        if (clients.isEmpty()) {
            System.out.println("Aucun client enregistré.");
            return;
        }
        afficherClientsTable(clients);
    }

    private void rechercherClient() {
        System.out.println("\n--- Rechercher un client ---");
        String terme = lireTexte("Terme de recherche (code, nom, adresse, téléphone) : ");
        List<Client> resultats = clientService.rechercherClients(terme);
        if (resultats.isEmpty()) {
            System.out.println("Aucun client trouvé.");
            return;
        }
        afficherClientsTable(resultats);
    }

    private void afficherHistoriqueReservation() {
        System.out.println("\n--- Historique de réservation ---");
        String code = lireTexte("Code client (CLTxxxx) : ");
        List<Reservation> historique = clientService.historiqueReservations(code);
        if (historique.isEmpty()) {
            System.out.println("Aucune réservation trouvée pour ce client ou client introuvable.");
            return;
        }
        afficherReservationsTable(historique);
    }

    private int lireEntier(String message) {
        while (true) {
            System.out.print(message);
            try {
                int valeur = Integer.parseInt(scanner.nextLine().trim());
                return valeur;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre entier valide.");
            }
        }
    }

    private String lireTexte(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    private void afficherClientsTable(List<Client> clients) {
        int codeWidth = Math.max("Code".length(), clients.stream().mapToInt(c -> c.getCode().length()).max().orElse(4));
        int nomWidth = Math.max("Nom".length(), clients.stream().mapToInt(c -> c.getNom().length()).max().orElse(3));
        int adresseWidth = Math.max("Adresse".length(), clients.stream().mapToInt(c -> c.getAdresse().length()).max().orElse(7));
        int telWidth = Math.max("Téléphone".length(), clients.stream().mapToInt(c -> c.getTelephone().length()).max().orElse(9));
        String format = String.format("%%-%ds | %%-%ds | %%-%ds | %%-%ds", codeWidth, nomWidth, adresseWidth, telWidth);
        System.out.println(String.format(format, "Code", "Nom", "Adresse", "Téléphone"));
        System.out.println(repeat('-', codeWidth + nomWidth + adresseWidth + telWidth + 9));
        for (Client client : clients) {
            System.out.println(String.format(format, client.getCode(), client.getNom(), client.getAdresse(), client.getTelephone()));
        }
    }

    private void afficherReservationsTable(List<Reservation> reservations) {
        int codeWidth = Math.max("Code".length(), reservations.stream().mapToInt(r -> r.getCode().length()).max().orElse(4));
        int chambreWidth = Math.max("Chambre".length(), reservations.stream().mapToInt(r -> r.getChambreCode().length()).max().orElse(6));
        int statutWidth = Math.max("Statut".length(), reservations.stream().mapToInt(r -> r.getStatut().length()).max().orElse(6));
        int dateWidth = Math.max("Début/Fin".length(), 10);
        int montantWidth = Math.max("Montant".length(), 8);
        String format = String.format("%%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds | %%-%ds", codeWidth, chambreWidth, statutWidth, dateWidth, dateWidth, montantWidth);
        System.out.println(String.format(format, "Code", "Chambre", "Statut", "Début", "Fin", "Montant"));
        System.out.println(repeat('-', codeWidth + chambreWidth + statutWidth + dateWidth * 2 + montantWidth + 15));
        for (Reservation reservation : reservations) {
            System.out.println(String.format(format,
                    reservation.getCode(),
                    reservation.getChambreCode(),
                    reservation.getStatut(),
                    reservation.getDateDebut(),
                    reservation.getDateFin(),
                    String.format("%.2f", reservation.getMontant())));
        }
    }

    private static String repeat(char ch, int count) {
        if (count <= 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            builder.append(ch);
        }
        return builder.toString();
    }
}

