package models;

public class Receptionniste extends Employe {
    public static final String SUFFIXE = "REC";
    private int nbClientsRecus;

    public Receptionniste(String matricule, String nom, String adresse, String telephone,
                          double salaire, String motDePasseHash, int nbClientsRecus) {
        super(matricule, nom, adresse, telephone, salaire, motDePasseHash, "RECEPTIONNISTE");
        this.nbClientsRecus = nbClientsRecus;
    }

    public int getNbClientsRecus() { return nbClientsRecus; }
    public void setNbClientsRecus(int nbClientsRecus) { this.nbClientsRecus = nbClientsRecus; }

    public void incrementerClients() { this.nbClientsRecus++; }

    @Override
    public String toString() {
        return matricule + "\t" + nom + "\t" + adresse + "\t" + telephone + "\t"
                + salaire + "\t" + motDePasseHash + "\t" + role + "\t" + nbClientsRecus;
    }
}