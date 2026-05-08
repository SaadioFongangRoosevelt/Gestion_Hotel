package ui;

import models.*;
import services.ReceptionnisteService;
import util.ConsoleUtil;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ReceptionnisteUI {
    private ReceptionnisteService service;
    private Receptionniste receptionniste;
    private Scanner scanner;

    public ReceptionnisteUI(Receptionniste receptionniste, Scanner scanner) {
        this.receptionniste = receptionniste;
        this.service = new ReceptionnisteService();
        this.scanner = scanner;
    }

    public void afficherMenu() {
        boolean continuer = true;
        while (continuer) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printTitle("MENU RÉCEPTIONNISTE");
            System.out.println("  Connecté : " + ConsoleUtil.CYAN + receptionniste.getNom()
                    + ConsoleUtil.RESET + " [" + receptionniste.getMatricule() + "]");
            ConsoleUtil.printSeparator();
            ConsoleUtil.printMenuOption(1, "Effectuer une réservation (+ enregistrer client)");
            ConsoleUtil.printMenuOption(2, "Supprimer un client");
            ConsoleUtil.printMenuOption(3, "Modifier les données d'un client");
            ConsoleUtil.printMenuOption(4, "Afficher la liste des clients");
            ConsoleUtil.printMenuOption(5, "Rechercher un client par code");
            ConsoleUtil.printMenuOption(6, "Consulter les chambres libres");
            ConsoleUtil.printMenuOption(0, "Déconnexion");
            ConsoleUtil.printSeparator();
            System.out.print("  Votre choix : ");

            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1": menuReservation(); break;
                case "2": menuSupprimerClient(); break;
                case "3": menuModifierClient(); break;
                case "4": afficherListeClients(); break;
                case "5": menuRechercherClient(); break;
                case "6": afficherChambresLibres(); break;
                case "0": continuer = false; break;
                default:
                    ConsoleUtil.error("Option invalide. Veuillez réessayer.");
                    pause();
            }
        }
    }

    private void menuReservation() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("NOUVELLE RÉSERVATION");
        try {
            List<Receptionniste> recs = new dao.EmployeDAO().lireReceptionnistes();
            if (recs.isEmpty()) {
                ConsoleUtil.error("Aucun réceptionniste enregistré. Réservation impossible.");
                pause(); return;
            }
            List<Chambre> libres = service.listerChambresLibres();
            if (libres.isEmpty()) {
                ConsoleUtil.error("Aucune chambre disponible. Réservation impossible.");
                pause(); return;
            }

            // Afficher les chambres libres
            ConsoleUtil.printSubTitle("CHAMBRES DISPONIBLES");
            ConsoleUtil.printTableHeader("Code", "Standing", "Prix/Nuit", "Service");
            for (Chambre c : libres) {
                ConsoleUtil.printTableRow(
                        c.getCodeChambre(), c.getStanding().name(),
                        c.getPrix() + " F", c.getServiceSupplementaire() + " F"
                );
            }
            System.out.println();

            // Choisir la chambre (boucle jusqu'au bon code)
            String codeChambre = null;
            Chambre chambreChoisie = null;
            while (chambreChoisie == null) {
                System.out.print("  Code chambre : ");
                codeChambre = scanner.nextLine().trim().toUpperCase();
                Chambre c = service.trouverChambre(codeChambre);
                if (c == null) {
                    ConsoleUtil.error("Code chambre introuvable. Réessayez.");
                } else if (c.estHorsService()) {
                    ConsoleUtil.error("Cette chambre est HORS SERVICE. Choisissez une autre.");
                } else if (!c.estLibre()) {
                    ConsoleUtil.error("Cette chambre est déjà OCCUPÉE. Choisissez une autre.");
                } else {
                    chambreChoisie = c;
                }
            }

            // Saisir les infos du client
            ConsoleUtil.printSubTitle("INFORMATIONS DU CLIENT");
            System.out.print("  Nom : ");
            String nom = scanner.nextLine().trim();
            System.out.print("  Adresse : ");
            String adresse = scanner.nextLine().trim();
            System.out.print("  Téléphone : ");
            String telephone = scanner.nextLine().trim();

            // Nombre de jours (boucle saisie valide)
            int nbreJours = 0;
            while (nbreJours <= 0) {
                System.out.print("  Nombre de jours : ");
                try {
                    nbreJours = Integer.parseInt(scanner.nextLine().trim());
                    if (nbreJours <= 0) ConsoleUtil.error("Le nombre de jours doit être positif.");
                } catch (NumberFormatException e) {
                    ConsoleUtil.error("Veuillez entrer un nombre valide.");
                }
            }

            Reservation res = service.effectuerReservation(nom, adresse, telephone,
                    chambreChoisie.getCodeChambre(), receptionniste.getMatricule(), nbreJours);

            ConsoleUtil.printSeparator();
            ConsoleUtil.success("Réservation effectuée avec succès !");
            System.out.println("  Code réservation : " + ConsoleUtil.PURPLE + res.getCodeReservation() + ConsoleUtil.RESET);
            System.out.println("  Code client      : " + ConsoleUtil.PURPLE + res.getCodeClient() + ConsoleUtil.RESET);
            System.out.println("  Montant total    : " + ConsoleUtil.PURPLE + res.getMontant() + " F" + ConsoleUtil.RESET);

            // Recharger le réceptionniste pour afficher le nouveau compteur
            Employe emp = new dao.EmployeDAO().trouverParMatricule(receptionniste.getMatricule());
            if (emp instanceof Receptionniste) {
                receptionniste = (Receptionniste) emp;
                System.out.println("  Clients reçus    : " + ConsoleUtil.PURPLE + receptionniste.getNbClientsRecus() + ConsoleUtil.RESET);
            }

        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuSupprimerClient() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("SUPPRIMER UN CLIENT");
        try {
            List<Client> clients = service.listerClients();
            if (clients.isEmpty()) {
                ConsoleUtil.error("Aucun client enregistré.");
                pause(); return;
            }
            afficherTableClients(clients);

            String code = saisirCodeClientValide("  Code du client à supprimer : ");
            System.out.print("  Confirmer la suppression de " + code + " ? (o/n) : ");
            String confirm = scanner.nextLine().trim();
            if ("o".equalsIgnoreCase(confirm)) {
                if (service.supprimerClient(code)) {
                    ConsoleUtil.success("Client " + code + " supprimé. Chambre remise à LIBRE.");
                } else {
                    ConsoleUtil.error("Client introuvable.");
                }
            } else {
                ConsoleUtil.info("Suppression annulée.");
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuModifierClient() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("MODIFIER UN CLIENT");
        try {
            List<Client> clients = service.listerClients();
            if (clients.isEmpty()) {
                ConsoleUtil.error("Aucun client enregistré.");
                pause(); return;
            }
            afficherTableClients(clients);

            String code = saisirCodeClientValide("  Code du client à modifier : ");
            Client c = service.rechercherClient(code);

            System.out.println("  Client : " + c.getNom() + " | " + c.getAdresse() + " | " + c.getTelephone());
            ConsoleUtil.printSeparator();
            ConsoleUtil.printMenuOption(1, "Modifier le nom");
            ConsoleUtil.printMenuOption(2, "Modifier l'adresse");
            ConsoleUtil.printMenuOption(3, "Modifier le téléphone");
            System.out.print("  Choix : ");
            String choix = scanner.nextLine().trim();

            switch (choix) {
                case "1":
                    System.out.print("  Nouveau nom : ");
                    String nom = scanner.nextLine().trim();
                    service.modifierNomClient(code, nom);
                    ConsoleUtil.success("Nom mis à jour.");
                    break;
                case "2":
                    System.out.print("  Nouvelle adresse : ");
                    String adresse = scanner.nextLine().trim();
                    service.modifierAdresseClient(code, adresse);
                    ConsoleUtil.success("Adresse mise à jour.");
                    break;
                case "3":
                    System.out.print("  Nouveau téléphone : ");
                    String tel = scanner.nextLine().trim();
                    service.modifierTelephoneClient(code, tel);
                    ConsoleUtil.success("Téléphone mis à jour.");
                    break;
                default:
                    ConsoleUtil.error("Choix invalide.");
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void afficherListeClients() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("LISTE DES CLIENTS");
        try {
            List<Client> clients = service.listerClients();
            if (clients.isEmpty()) {
                ConsoleUtil.info("Aucun client enregistré.");
            } else {
                afficherTableClients(clients);
                System.out.println();
                ConsoleUtil.stat("Total clients", clients.size());
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuRechercherClient() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("RECHERCHER UN CLIENT");
        try {
            String code = saisirCodeClientValide("  Code du client : ");
            Client c = service.rechercherClient(code);
            ConsoleUtil.printSubTitle("RÉSULTAT");
            ConsoleUtil.printTableHeader("Code", "Nom", "Adresse", "Téléphone");
            ConsoleUtil.printTableRow(c.getCodeClient(), c.getNom(), c.getAdresse(), c.getTelephone());
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void afficherChambresLibres() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("CHAMBRES LIBRES");
        try {
            List<Chambre> libres = service.listerChambresLibres();
            if (libres.isEmpty()) {
                ConsoleUtil.info("Aucune chambre libre pour le moment.");
            } else {
                ConsoleUtil.printTableHeader("Code", "Standing", "Prix/Nuit", "Service Supp.");
                for (Chambre c : libres) {
                    ConsoleUtil.printTableRow(
                            c.getCodeChambre(), c.getStanding().name(),
                            c.getPrix() + " F", c.getServiceSupplementaire() + " F"
                    );
                }
                System.out.println();
                ConsoleUtil.stat("Total chambres libres", libres.size());
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    // ---- Helpers ----

    private void afficherTableClients(List<Client> clients) {
        ConsoleUtil.printTableHeader("Code", "Nom", "Adresse", "Téléphone");
        for (Client c : clients) {
            ConsoleUtil.printTableRow(c.getCodeClient(), c.getNom(), c.getAdresse(), c.getTelephone());
        }
    }

    /**
     * Boucle jusqu'à ce que l'utilisateur entre un code client existant.
     */
    private String saisirCodeClientValide(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String code = scanner.nextLine().trim().toUpperCase();
            Client c = service.rechercherClient(code);
            if (c != null) return code;
            ConsoleUtil.error("Code client introuvable. Réessayez.");
        }
    }

    private void pause() {
        System.out.print("\n  Appuyez sur ENTRÉE pour continuer...");
        scanner.nextLine();
    }
}
