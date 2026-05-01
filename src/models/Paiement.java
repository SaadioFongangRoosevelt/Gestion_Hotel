package models;

public class Paiement {
    private double montant;
    private String date;
    private Client client;
    private Reservation reservation;    

    public Paiement(double montant, String date, Client client, Reservation reservation) {
        this.montant = montant;
        this.date = date;
        this.client = client;
        this.reservation = reservation;
    }

    public double getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }

    public Client getClient() {
        return client;
    }

    public Reservation getReservation() {
        return reservation;
    }
}
