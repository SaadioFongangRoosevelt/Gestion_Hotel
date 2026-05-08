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

    public String getCodeReservation() { 
        return codeReservation; 
    }
    public String getCodeClient() { 
        return codeClient; 
    }
    public String getMatriculeReceptionniste() {
         return matriculeReceptionniste; 
    }
    public String getCodeChambre() {
         return codeChambre; 
        }
    public double getMontant() { 
        return montant; 
    }
    public LocalDate getDateReservation() { 
        return dateReservation; 
    }
    public int getNombreJours() { 
        return nombreJours;
     }
    public void setCodeReservation(String code){
        this.codeReservation=code;
    }
    public void setCodeClient(String code){
        this.codeClient=code;
    }
    public void setMatriculeReceptionniste(String matricule){
        this.matriculeReceptionniste=matricule;
    }
    public void setCodeChambre(String code){
        this.codeChambre=code;
    }
    public void setMontant(double montant){
        this.montant=montant;
    }
    public void setDateReservation(LocalDate date){
        this.dateReservation=date;
    }
    public void setNombreJours(int jours){
        this.nombreJours=jours;
    }       

    @Override
    public String toString() {
        return codeReservation + "\t" + codeClient + "\t" + matriculeReceptionniste + "\t"
                + codeChambre + "\t" + montant + "\t" + dateReservation.toString() + "\t" + nombreJours;
    }
}
