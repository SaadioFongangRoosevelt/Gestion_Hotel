package ui;

import models.*;
import services.GerantService;
import util.ConsoleUtil;
import util.PasswordUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
public class GerantUI {

    private GerantService service;
    private Gerant gerant;
    private Scanner scanner;

    public GerantUI(Gerant gerant, Scanner scanner) {
        this.gerant = gerant;
        this.service = new GerantService();
        this.scanner = scanner;
    }

    private void menuRecruterReceptionniste() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("RECRUTER UN RÉCEPTIONNISTE");
        try {
            System.out.print("  Nom : ");
            String nom = scanner.nextLine().trim();
            System.out.print("  Adresse : ");
            String adresse = scanner.nextLine().trim();
            System.out.print("  Téléphone : ");
            String telephone = scanner.nextLine().trim();

            double salaire = 0;
            while (salaire <= 0) {
                System.out.print("  Salaire (F) : ");
                try {
                    salaire = Double.parseDouble(scanner.nextLine().trim());
                    if (salaire <= 0) ConsoleUtil.error("Le salaire doit être positif.");
                } catch (NumberFormatException e) {
                    ConsoleUtil.error("Valeur numérique invalide.");
                }
            }

            String motDePasse = PasswordUtil.readPassword("  Mot de passe : ");
            while (motDePasse == null || motDePasse.isBlank()) {
                ConsoleUtil.error("Le mot de passe ne peut pas être vide.");
                motDePasse = PasswordUtil.readPassword("  Mot de passe : ");
            }

            Receptionniste rec = service.recruterReceptionniste(nom, adresse, telephone, salaire, motDePasse);
            ConsoleUtil.success("Réceptionniste recruté avec succès !");
            System.out.println("  Matricule attribué : " + ConsoleUtil.PURPLE + rec.getMatricule() + ConsoleUtil.RESET);

        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuPayerReceptionniste() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("PAYER UN RÉCEPTIONNISTE");
        try {
            List<Receptionniste> recs = service.listerReceptionnistes();
            if (recs.isEmpty()) {
                ConsoleUtil.error("Aucun réceptionniste enregistré.");
                pause(); return;
            }
            afficherTableReceptionnistes(recs);

            String matricule = saisirMatriculeReceptionnisteValide("  Matricule du réceptionniste : ");
            Receptionniste rec = service.trouverReceptionniste(matricule);

            System.out.println("\n  " + rec.getNom() + " — Salaire de base : " + ConsoleUtil.PURPLE
                    + rec.getSalaire() + " F" + ConsoleUtil.RESET);
            System.out.println("  Clients reçus (avant paiement) : " + ConsoleUtil.PURPLE
                    + rec.getNbClientsRecus() + ConsoleUtil.RESET);

            int quotas = rec.getNbClientsRecus() / 10;
            double bonus = quotas * 2000;
            System.out.println("  Quotas atteints : " + ConsoleUtil.PURPLE + quotas
                    + " × 2000 F = " + bonus + " F bonus" + ConsoleUtil.RESET);

            double totalPaye = service.payerReceptionniste(matricule);
            ConsoleUtil.printSeparator();
            ConsoleUtil.success("Paiement effectué !");
            System.out.println("  Montant total versé : " + ConsoleUtil.PURPLE + totalPaye + " F" + ConsoleUtil.RESET);
            ConsoleUtil.info("Compteur de clients remis à 0.");

        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuAjouterChambre() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("AJOUTER UNE CHAMBRE");
        try {
            ConsoleUtil.printMenuOption(1, "STANDARD (service supp.: 2000 F)");
            ConsoleUtil.printMenuOption(2, "VIP (service supp.: 5000 F)");
            ConsoleUtil.printMenuOption(3, "SUITE (service supp.: 10000 F)");
            System.out.print("  Choisir le standing : ");

            Chambre.Standing standing = null;
            while (standing == null) {
                String choix = scanner.nextLine().trim();
                switch (choix) {
                    case "1": standing = Chambre.Standing.STANDARD; break;
                    case "2": standing = Chambre.Standing.VIP; break;
                    case "3": standing = Chambre.Standing.SUITE; break;
                    default: ConsoleUtil.error("Choix invalide, entrez 1, 2 ou 3.");
                        System.out.print("  Choisir le standing : ");
                }
            }

            double prix = 0;
            while (prix <= 0) {
                System.out.print("  Prix par nuit (F) : ");
                try {
                    prix = Double.parseDouble(scanner.nextLine().trim());
                    if (prix <= 0) ConsoleUtil.error("Le prix doit être positif.");
                } catch (NumberFormatException e) {
                    ConsoleUtil.error("Valeur numérique invalide.");
                }
            }

            Chambre chambre = service.ajouterChambre(standing, prix);
            ConsoleUtil.success("Chambre ajoutée avec succès !");
            System.out.println("  Code attribué : " + ConsoleUtil.PURPLE + chambre.getCodeChambre() + ConsoleUtil.RESET);

        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuMettreHorsService() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("METTRE UNE CHAMBRE HORS SERVICE");
        try {
            List<Chambre> chambres = service.listerChambres();
            if (chambres.isEmpty()) {
                ConsoleUtil.error("Aucune chambre enregistrée.");
                pause(); return;
            }
            afficherTableChambres(chambres);

            String code = saisirCodeChambreValide("  Code de la chambre : ");
            if (service.mettreChAmbreHorsService(code)) {
                ConsoleUtil.success("Chambre " + code + " mise HORS SERVICE.");
            } else {
                ConsoleUtil.error("Erreur lors de la mise hors service.");
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void menuRemettreEnService() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("REMETTRE UNE CHAMBRE EN SERVICE");
        try {
            List<Chambre> chambres = service.listerChambres();
            List<Chambre> horsService = new java.util.ArrayList<>();
            for (Chambre c : chambres) {
                if (c.estHorsService()) horsService.add(c);
            }
            if (horsService.isEmpty()) {
                ConsoleUtil.info("Aucune chambre hors service.");
                pause(); return;
            }
            afficherTableChambres(horsService);

            String code = saisirCodeChambreHorsServiceValide("  Code de la chambre à remettre en service : ");
            if (service.remettreEnService(code)) {
                ConsoleUtil.success("Chambre " + code + " remise en service (LIBRE).");
            } else {
                ConsoleUtil.error("Erreur lors de la remise en service.");
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    @SuppressWarnings("unchecked")
    private void afficherStatistiques() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("STATISTIQUES GÉNÉRALES");
        try {
            Map<String, Object> stats = service.getStatistiques();

            ConsoleUtil.printSubTitle("CLIENTS");
            ConsoleUtil.stat("Nombre total de clients", stats.get("nbClients"));
            List<Client> clients = (List<Client>) stats.get("clients");
            if (!clients.isEmpty()) {
                System.out.println();
                ConsoleUtil.printTableHeader("Code", "Nom", "Adresse", "Téléphone");
                for (Client c : clients) {
                    ConsoleUtil.printTableRow(c.getCodeClient(), c.getNom(), c.getAdresse(), c.getTelephone());
                }
            }

            ConsoleUtil.printSubTitle("RÉCEPTIONNISTES");
            ConsoleUtil.stat("Nombre de réceptionnistes", stats.get("nbReceptionnistes"));
            List<Receptionniste> recs = (List<Receptionniste>) stats.get("receptionnistes");
            if (!recs.isEmpty()) {
                System.out.println();
                afficherTableReceptionnistes(recs);
            }

            ConsoleUtil.printSubTitle("CHAMBRES");
            ConsoleUtil.stat("Total chambres", stats.get("nbChambres"));
            ConsoleUtil.stat("Standard", stats.get("nbStandard"));
            ConsoleUtil.stat("VIP", stats.get("nbVip"));
            ConsoleUtil.stat("Suite", stats.get("nbSuite"));
            System.out.println();
            ConsoleUtil.stat("Libres", stats.get("nbLibres"));
            ConsoleUtil.stat("Occupées", stats.get("nbOccupees"));
            ConsoleUtil.stat("Hors service", stats.get("nbHorsService"));

        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void afficherListeReceptionnistes() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("LISTE DES RÉCEPTIONNISTES");
        try {
            List<Receptionniste> recs = service.listerReceptionnistes();
            if (recs.isEmpty()) {
                ConsoleUtil.info("Aucun réceptionniste enregistré.");
            } else {
                afficherTableReceptionnistes(recs);
                System.out.println();
                ConsoleUtil.stat("Total", recs.size());
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void afficherListeChambres() {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("LISTE DES CHAMBRES");
        try {
            List<Chambre> chambres = service.listerChambres();
            if (chambres.isEmpty()) {
                ConsoleUtil.info("Aucune chambre enregistrée.");
            } else {
                afficherTableChambres(chambres);
                System.out.println();
                ConsoleUtil.stat("Total", chambres.size());
            }
        } catch (IOException e) {
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void afficherTableReceptionnistes(List<Receptionniste> recs) {
        ConsoleUtil.printTableHeader("Matricule", "Nom", "Salaire", "Clients Reçus");
        for (Receptionniste r : recs) {
            ConsoleUtil.printTableRow(r.getMatricule(), r.getNom(),
                    r.getSalaire() + " F", String.valueOf(r.getNbClientsRecus()));
        }
    }

    private void afficherTableChambres(List<Chambre> chambres) {
        ConsoleUtil.printTableHeader("Code", "Standing", "Statut", "Prix/Nuit");
        for (Chambre c : chambres) {
            ConsoleUtil.printTableRow(c.getCodeChambre(), c.getStanding().name(),
                    c.getStatut().name(), c.getPrix() + " F");
        }
    }

    private String saisirMatriculeReceptionnisteValide(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String mat = scanner.nextLine().trim().toUpperCase();
            Receptionniste r = service.trouverReceptionniste(mat);
            if (r != null) return mat;
            ConsoleUtil.error("Matricule introuvable. Réessayez.");
        }
    }

    private String saisirCodeChambreValide(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String code = scanner.nextLine().trim().toUpperCase();
            Chambre c = service.trouverChambre(code);
            if (c != null) return code;
            ConsoleUtil.error("Code chambre introuvable. Réessayez.");
        }
    }

    private String saisirCodeChambreHorsServiceValide(String prompt) throws IOException {
        while (true) {
            System.out.print(prompt);
            String code = scanner.nextLine().trim().toUpperCase();
            Chambre c = service.trouverChambre(code);
            if (c != null && c.estHorsService()) return code;
            if (c == null) ConsoleUtil.error("Code chambre introuvable. Réessayez.");
            else ConsoleUtil.error("Cette chambre n'est pas hors service. Réessayez.");
        }
    }


    private void menuLicencierReceptionniste()
    {
        ConsoleUtil.clearScreen();
        ConsoleUtil.printTitle("LICENCIEMENT D'UN EMPLOYE");
        try{
           List <Receptionniste> recep = service.listerReceptionnistes();
           if (recep.isEmpty())
           {
               ConsoleUtil.error("Aucun receptionniste n'a ete recrute pour effectuer un quelconque licenciement :- ");
               pause(); return;
           }
           afficherTableReceptionnistes(recep);
           String matricule = saisirMatriculeReceptionnisteValide("Entrer le matricule du receptionniste a licencier: ");
           Receptionniste r = service.trouverReceptionniste(matricule);

            System.out.println("\n "+ConsoleUtil.YELLOW + "Vous etes sur le point de licencier: "+ r.getNom() + "[" + r.getMatricule() +"]");
            System.out.println("Confimer le licenciement ? (o/n)");
            String confirmation = scanner.nextLine().trim().toUpperCase();
            if ("O".equals(confirmation)) {
                if (service.licencierReceptionniste(matricule))
                {
                    ConsoleUtil.success("Receptionniste licencie avec succes");
                }else{
                    ConsoleUtil.error("Echec du licenciement.");
                }
            }else {
                ConsoleUtil.info("Licenciement annule");
            }
        }catch (IOException e){
            ConsoleUtil.error("Erreur : " + e.getMessage());
        }
        pause();
    }

    private void pause() {
        System.out.print("\n  Appuyez sur ENTRÉE pour continuer...");
        scanner.nextLine();
    }

    public void afficherMenu() {
        boolean continuer = true;
        while (continuer) {
            ConsoleUtil.clearScreen();
            ConsoleUtil.printTitle("MENU GÉRANT");
            System.out.println("  Connecté : " + ConsoleUtil.CYAN + gerant.getNom()
                    + ConsoleUtil.RESET + " [" + gerant.getMatricule() + "]");
            ConsoleUtil.printSeparator();
            ConsoleUtil.printMenuOption(1, "Recruter un réceptionniste");
            ConsoleUtil.printMenuOption(2, "Payer un réceptionniste");
            ConsoleUtil.printMenuOption(3, "Ajouter une chambre");
            ConsoleUtil.printMenuOption(4, "Mettre une chambre hors service");
            ConsoleUtil.printMenuOption(5, "Remettre une chambre en service");
            ConsoleUtil.printMenuOption(6, "Statistiques générales");
            ConsoleUtil.printMenuOption(7, "Liste des réceptionnistes");
            ConsoleUtil.printMenuOption(8, "Liste des chambres");
            ConsoleUtil.printMenuOption(9, "Licencier un receptionniste");
            ConsoleUtil.printMenuOption(0, "Déconnexion");
            ConsoleUtil.printSeparator();
            System.out.print("  Votre choix : ");

            String choix = scanner.nextLine().trim();
            switch (choix) {
                case "1": menuRecruterReceptionniste(); break;
                case "2": menuPayerReceptionniste(); break;
                case "3": menuAjouterChambre(); break;
                case "4": menuMettreHorsService(); break;
                case "5": menuRemettreEnService(); break;
                case "6": afficherStatistiques(); break;
                case "7": afficherListeReceptionnistes(); break;
                case "8": afficherListeChambres(); break;
                case "9": menuLicencierReceptionniste(); break;
                case "0": continuer = false; break;
                default:
                    ConsoleUtil.error("Option invalide. Réessayez.");
                    pause();
            }
        }
    }

}
