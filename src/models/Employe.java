package models;

public abstract class Employe extends Personne {
    protected String matricule;
    protected double salaire;
    protected String motDePasseHash;
    protected String role;

    public Employe(String matricule, String nom, String adresse, String telephone,
                   double salaire, String motDePasseHash, String role) {
        super(nom, adresse, telephone);
        this.matricule = matricule;
        this.salaire = salaire;
        this.motDePasseHash = motDePasseHash;
        this.role = role;
    }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public double getSalaire() { return salaire; }
    public void setSalaire(double salaire) { this.salaire = salaire; }

    public String getMotDePasseHash() { return motDePasseHash; }
    public void setMotDePasseHash(String motDePasseHash) { this.motDePasseHash = motDePasseHash; }

    public String getRole() { return role; }
}
