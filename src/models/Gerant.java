package models;

public class Gerant extends Employe {
    public static final String SUFFIXE = "GER";

    public Gerant(String matricule, String nom, String adresse, String telephone,
                  double salaire, String motDePasseHash) {
        super(matricule, nom, adresse, telephone, salaire, motDePasseHash, "GERANT");
    }

    @Override
    public String toString() {
        return matricule + "\t" + nom + "\t" + adresse + "\t" + telephone + "\t"
                + salaire + "\t" + motDePasseHash + "\t" + role;
    }
}