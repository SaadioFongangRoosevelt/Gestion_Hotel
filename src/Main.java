import models.*;
import services.AuthService;
import services.InitService;
import ui.GerantUI;
import ui.ReceptionnisteUI;
import util.ConsoleUtil;
import util.PasswordUtil;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static InitService initService = new InitService();
    private static AuthService authService = new AuthService();

    public static void main(String[] args) {
        // Créer le dossier data si inexistant
        new File("data").mkdirs();

        PasswordUtil.setFallbackScanner(scanner);

        try {
            afficherBanniere();

            // Premier lancement : création du gerant
            if (!initService.gerantExiste()) {
                ConsoleUtil.info("Premier lancement détecté. Création du compte gérant.");
                creerGerantInitial();
            }

            // Forcer connexion gérant s'il n'y a pas encore de réceptionniste
            boolean boucleConnexion = true;
            while (boucleConnexion) {
                try {
                    if (!initService.aDesReceptionnistes()) {
                        ConsoleUtil.info("Aucun réceptionniste trouvé. Connexion en mode GÉRANT obligatoire.");
                        ConsoleUtil.info("Veuillez vous connecter pour ajouter des réceptionnistes.");
                    }

                    Employe employe = seConnecter();
                    if (employe == null) {
                        ConsoleUtil.error("Connexion échouée. Réessayez.");
                        pause();
                        continue;
                    }

                    // Vérification : si pas de réceptionniste, forcer mode gérant
                    if (!initService.aDesReceptionnistes() && !(employe instanceof Gerant)) {
                        ConsoleUtil.error("Aucun réceptionniste enregistré. Connexion en mode GÉRANT uniquement.");
                        pause();
                        continue;
                    }


                    if (employe instanceof Gerant) {
                        new GerantUI((Gerant) employe, scanner).afficherMenu();
                    } else if (employe instanceof Receptionniste) {
                        new ReceptionnisteUI((Receptionniste) employe, scanner).afficherMenu();
                    }

                    // Après déconnexion, redemander connexion
                    ConsoleUtil.clearScreen();
                    afficherBanniere();
                    ConsoleUtil.info("Déconnexion effectuée.");
                    System.out.print("\n  Voulez-vous vous reconnecter ? (o/n) : ");
                    String rep = scanner.nextLine().trim();
                    if (!"o".equalsIgnoreCase(rep)) {
                        boucleConnexion = false;
                        ConsoleUtil.printTitle("AU REVOIR !");
                    }

                } catch (IOException e) {
                    ConsoleUtil.error("Erreur système : " + e.getMessage());
                    pause();
                }
            }

        } catch (Exception e) {
            ConsoleUtil.error("Erreur critique : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void afficherBanniere() {
        ConsoleUtil.clearScreen();
        System.out.println(ConsoleUtil.CYAN + ConsoleUtil.BOLD);
        System.out.println("  ╔══════════════════════════════════════════════════════════╗");
        System.out.println("  ║                  BIENVENUE DANS LE                       ║");
        System.out.println("  ║        🏨  SYSTEME DE GESTION HOTELIERE  🏨              ║");
        System.out.println("  ║                                                          ║");
        System.out.println("  ║          Programmation Orientee Objet — Java             ║");
        System.out.println("  ╚══════════════════════════════════════════════════════════╝");
        System.out.println(ConsoleUtil.RESET);
    }

    private static void creerGerantInitial() throws IOException {
        ConsoleUtil.printTitle("CRÉATION DU GÉRANT");
        System.out.println("  Veuillez saisir les informations du gérant :\n");

        System.out.print("  Nom complet : ");
        String nom = scanner.nextLine().trim();

        System.out.print("  Adresse : ");
        String adresse = scanner.nextLine().trim();

        System.out.print("  Téléphone : ");
        String telephone = scanner.nextLine().trim();

        double salaire = 0;
        while (salaire <= 0) {
            System.out.print("  Salaire (FC) : ");
            try {
                salaire = Double.parseDouble(scanner.nextLine().trim());
                if (salaire <= 0) ConsoleUtil.error("Le salaire doit être positif.");
            } catch (NumberFormatException e) {
                ConsoleUtil.error("Valeur invalide. Veuillez entrer un nombre.");
            }
        }

        String motDePasse = "";
        while (motDePasse.isBlank()) {
            motDePasse = PasswordUtil.readPassword("  Mot de passe : ");
            if (motDePasse.isBlank()) ConsoleUtil.error("Le mot de passe ne peut pas être vide.");
        }

        String confirmation = PasswordUtil.readPassword("  Confirmez le mot de passe : ");
        while (!motDePasse.equals(confirmation)) {
            ConsoleUtil.error("Les mots de passe ne correspondent pas. Réessayez.");
            motDePasse = PasswordUtil.readPassword("  Mot de passe : ");
            confirmation = PasswordUtil.readPassword("  Confirmez le mot de passe : ");
        }

        Gerant gerant = initService.creerGerant(nom, adresse, telephone, salaire, motDePasse);
        ConsoleUtil.success("Compte gérant créé avec succes !");
        System.out.println("  Matricule : " + ConsoleUtil.PURPLE + gerant.getMatricule() + ConsoleUtil.RESET);
        System.out.println("  Notez bien ce matricule pour vous connecter.");
        pause();
    }

    private static Employe seConnecter() throws IOException {
        ConsoleUtil.printTitle("CONNEXION");
        System.out.print("  Matricule : ");
        String matricule = scanner.nextLine().trim().toUpperCase();

        String motDePasse = PasswordUtil.readPassword("  Mot de passe : ");

        Employe employe = authService.authentifier(matricule, motDePasse);
        if (employe != null) {
            ConsoleUtil.success("Connexion reussie ! Bienvenue, " + employe.getNom()
                    + " [" + employe.getRole() + "]");
            pause();
        }
        return employe;
    }

    private static void pause() {
        System.out.print("\n  Appuyez sur ENTRÉE pour continuer...");
        scanner.nextLine();
    }
}
