package services;

import dao.*;
import models.*;
import util.MatriculeGenerator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ReceptionnisteService {
    private ClientDAO clientDAO;
    private ChambreDAO chambreDAO;
    private ReservationDAO reservationDAO;
    private EmployeDAO employeDAO;
    private CompteurDAO compteurDAO;

    public ReceptionnisteService() {
        this.clientDAO = new ClientDAO();
        this.chambreDAO = new ChambreDAO();
        this.reservationDAO = new ReservationDAO();
        this.employeDAO = new EmployeDAO();
        this.compteurDAO = new CompteurDAO();
    }

    /**
     * Enregistre une réservation : crée le client, la réservation,
     * met à jour la chambre et incrémente le compteur du réceptionniste.
     */
    public Reservation effectuerReservation(String nomClient, String adresseClient,
                                            String telClient, String codeChambre,
                                            String matriculeRec, int nombreJours) throws IOException {
        // Créer le client
        int numClient = compteurDAO.prochainClient();
        String codeClient = MatriculeGenerator.genererCodeClient(numClient);
        Client client = new Client(codeClient, nomClient, adresseClient, telClient);
        clientDAO.sauvegarder(client);

        // Calculer le montant
        Chambre chambre = chambreDAO.trouverParCode(codeChambre);
        double montant = nombreJours * (chambre.getPrix() + chambre.getServiceSupplementaire());

        // Créer la réservation
        int numRes = compteurDAO.prochainReservation();
        String codeRes = MatriculeGenerator.genererCodeReservation(numRes);
        Reservation reservation = new Reservation(codeRes, codeClient, matriculeRec,
                codeChambre, montant, LocalDate.now(), nombreJours);
        reservationDAO.sauvegarder(reservation);

        // Marquer la chambre comme occupée
        chambre.setStatut(Chambre.Statut.OCCUPEE);
        chambreDAO.mettreAJour(chambre);

        // Incrémentation du     compteur du réceptionniste
        Employe e = employeDAO.trouverParMatricule(matriculeRec);
        if (e instanceof Receptionniste) {
            ((Receptionniste) e).incrementerClients();
            employeDAO.mettreAJour(e);
        }

        return reservation;
    }

    public boolean supprimerClient(String codeClient) throws IOException {
        // Trouver la réservation liée pour libérer la chambre
        Reservation res = reservationDAO.trouverParCodeClient(codeClient);
        if (res != null) {
            Chambre chambre = chambreDAO.trouverParCode(res.getCodeChambre());
            if (chambre != null) {
                chambre.setStatut(Chambre.Statut.LIBRE);
                chambreDAO.mettreAJour(chambre);
            }
            reservationDAO.supprimerParCodeClient(codeClient);
          }
        return clientDAO.supprimer(codeClient);
    }

    public boolean modifierNomClient(String codeClient, String nouveauNom) throws IOException {
        Client c = clientDAO.trouverParCode(codeClient);
        if (c == null) return false;
        c.setNom(nouveauNom);
        return clientDAO.mettreAJour(c);
    }

    public boolean modifierAdresseClient(String codeClient, String nouvelleAdresse) throws IOException {
        Client c = clientDAO.trouverParCode(codeClient);
        if (c == null) return false;
        c.setAdresse(nouvelleAdresse);
        return clientDAO.mettreAJour(c);
    }

    public boolean modifierTelephoneClient(String codeClient, String nouveauTel) throws IOException {
        Client c = clientDAO.trouverParCode(codeClient);
        if (c == null) return false;
        c.setTelephone(nouveauTel);
        return clientDAO.mettreAJour(c);
    }

    public List<Client> listerClients() throws IOException {
        return clientDAO.lireTous();
    }

    public Client rechercherClient(String codeClient) throws IOException {
        return clientDAO.trouverParCode(codeClient);
    }

    public List<Chambre> listerChambresLibres() throws IOException {
        return chambreDAO.lireLibres();
    }

    public Chambre trouverChambre(String code) throws IOException {
        return chambreDAO.trouverParCode(code);
    }

    public List<Chambre> listerToutesChambres() throws IOException {
        return chambreDAO.lireTous();
    }
}

