package models;

public class Reservation {
    private String code;
    private String clientCode;
    private String chambreCode;
    private String statut;
    private String dateDebut;
    private String dateFin;
    private double montant;

    public Reservation() {
    }

    public Reservation(String code, String clientCode, String chambreCode, String statut, String dateDebut, String dateFin, double montant) {
        this.code = code;
        this.clientCode = clientCode;
        this.chambreCode = chambreCode;
        this.statut = statut;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getChambreCode() {
        return chambreCode;
    }

    public void setChambreCode(String chambreCode) {
        this.chambreCode = chambreCode;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public String enregistrer() {
        return String.join("\t", code, clientCode, chambreCode, statut, dateDebut, dateFin, String.valueOf(montant));
    }

    public static Reservation depuisEnregistrement(String ligne) {
        String[] parts = ligne.split("\t");
        if (parts.length != 7) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setCode(parts[0]);
        reservation.setClientCode(parts[1]);
        reservation.setChambreCode(parts[2]);
        reservation.setStatut(parts[3]);
        reservation.setDateDebut(parts[4]);
        reservation.setDateFin(parts[5]);
        try {
            reservation.setMontant(Double.parseDouble(parts[6]));
        } catch (NumberFormatException e) {
            reservation.setMontant(0);
        }
        return reservation;
    }
}
