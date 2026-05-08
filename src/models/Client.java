package models;

public class Client extends Personne {
    private String codeClient;

    public Client(String codeClient, String nom, String adresse, String telephone) {
        super(nom, adresse, telephone);
        this.codeClient = codeClient;
    }

    public String getCodeClient() { return codeClient; }
    public void setCodeClient(String codeClient) { this.codeClient = codeClient; }

    @Override
    public String toString()
    {
        return codeClient + "\t" + nom + "\t" + adresse + "\t" + telephone;
    }
}
