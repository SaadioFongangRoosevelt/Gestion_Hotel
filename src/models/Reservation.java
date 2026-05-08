package models;

import java.time.LocalDate;

public class Reservation {
    private String codeReservation;
    private String codeClient;
    private String matriculeReceptionniste;
    private String codeChambre;
    private double montant;
    private LocalDate dateReservation;
    private int nombreJours;

    public Reservation(String codeReservation, String codeClient, String matriculeReceptionniste,
                       String codeChambre, double montant, LocalDate dateReservation, int nombreJours) {
        this.codeReservation = codeReservation;
        this.codeClient = codeClient;
        this.matriculeReceptionniste = matriculeReceptionniste;
        this.codeChambre = codeChambre;
        this.montant = montant;
        this.dateReservation = dateReservation;
        this.nombreJours = nombreJours;
    }

    public String getCodeReservation() { return codeReservation; }
    public String getCodeClient() { return codeClient; }
    public String getMatriculeReceptionniste() { return matriculeReceptionniste; }
    public String getCodeChambre() { return codeChambre; }
    public double getMontant() { return montant; }
    public LocalDate getDateReservation() { return dateReservation; }
    public int getNombreJours() { return nombreJours; }
    public void setCodeReservation(String codeReservation) { this.codeReservation = codeReservation; }
    public void setCodeClient(String codeClient) { this.codeClient = codeClient; }
    public void setMatriculeReceptionniste(String matriculeReceptionniste) { this.matriculeReceptionniste = matriculeReceptionniste; }
    public void setCodeChambre(String codeChambre) { this.codeChambre = codeChambre; }
    public void setMontant(double montant) { this.montant = montant; }
    public void setDateReservation(LocalDate dateReservation) { this.dateReservation = dateReservation; }
    public void setNombreJours(int nombreJours) { this.nombreJours = nombreJours; }

    @Override
    public String toString() {
        return codeReservation + "\t" + codeClient + "\t" + matriculeReceptionniste + "\t"
                + codeChambre + "\t" + montant + "\t" + dateReservation.toString() + "\t" + nombreJours;
    }
}
