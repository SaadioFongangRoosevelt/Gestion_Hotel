package models;

public class Chambre {

    public enum Standing { STANDARD, VIP, SUITE }
    public enum Statut { LIBRE, OCCUPEE, HORS_SERVICE }

    private String codeChambre;
    private Standing standing;
    private Statut statut;
    private double prix;

    public Chambre(String codeChambre, Standing standing, Statut statut, double prix) {
        this.codeChambre = codeChambre;
        this.standing = standing;
        this.statut = statut;
        this.prix = prix;
    }

    public String getCodeChambre() { return codeChambre; }
    public Standing getStanding() { return standing; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public double getPrix() { return prix; }
    public void setPrix(double prix) { this.prix = prix; }

}
