package models;

public class Personne {
    private String code;
    private String nom;
    private String adresse;
    private String telephone;

    public Personne() {
    }

    public Personne(String code, String nom, String adresse, String telephone) {
        this.code = code;
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String enregistrer() {
        return String.join("\t", code, nom, adresse, telephone);
    }
}

