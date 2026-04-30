package service;

import dao.ClientDAO;
import dao.ReservationDAO;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import models.Client;
import models.Reservation;

public class ClientService {
    private final ClientDAO clientDAO;
    private final ReservationDAO reservationDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
        this.reservationDAO = new ReservationDAO();
    }

    public Client creerClient(String nom, String adresse, String telephone) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }
        Client client = new Client(null, nom.trim(), adresse == null ? "" : adresse.trim(), telephone == null ? "" : telephone.trim());
        return clientDAO.ajouter(client);
    }

    public List<Client> listerClients() {
        return clientDAO.chercherTous();
    }

    public Optional<Client> chercherClientParCode(String code) {
        return clientDAO.chercherParCode(code);
    }

    public List<Client> rechercherClients(String terme) {
        return clientDAO.rechercher(terme);
    }

    public boolean modifierClient(String code, String nouveauNom, String nouvelleAdresse, String nouveauTelephone) {
        Optional<Client> optional = clientDAO.chercherParCode(code);
        if (optional.isEmpty()) {
            return false;
        }
        Client client = optional.get();
        if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
            client.setNom(nouveauNom.trim());
        }
        if (nouvelleAdresse != null) {
            client.setAdresse(nouvelleAdresse.trim());
        }
        if (nouveauTelephone != null) {
            client.setTelephone(nouveauTelephone.trim());
        }
        return clientDAO.modifier(client);
    }

    public boolean supprimerClient(String code) {
        return clientDAO.supprimer(code);
    }

    public List<Reservation> historiqueReservations(String code) {
        Optional<Client> optional = clientDAO.chercherParCode(code);
        if (optional.isEmpty()) {
            return Collections.emptyList();
        }
        return reservationDAO.trouverParClient(optional.get().getCode());
    }
}

