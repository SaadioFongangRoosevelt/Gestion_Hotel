package models;

public class Client extends Personne {
    public Client() {
        super();
    }

    public Client(String code, String nom, String adresse, String telephone) {
        super(code, nom, adresse, telephone);
    }

    public static Client depuisEnregistrement(String ligne) {
        String[] parts = ligne.split("\t");
        if (parts.length != 4) {
            return null;
        }
        return new Client(parts[0], parts[1], parts[2], parts[3]);
    }
}

