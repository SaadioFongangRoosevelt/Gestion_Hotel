package services;

import dao.CompteurDAO;
import dao.EmployeDAO;
import models.Gerant;
import util.MatriculeGenerator;
import util.PasswordUtil;

import java.io.IOException;

public class InitService {
    private CompteurDAO compteurDAO;
    private EmployeDAO employeDAO;

    public InitService() {
        this.compteurDAO = new CompteurDAO();
        this.employeDAO = new EmployeDAO();
    }


    public boolean gerantExiste() {
        return compteurDAO.gerantExiste();
    }

    public boolean aDesReceptionnistes() throws IOException {
        return !employeDAO.lireReceptionnistes().isEmpty();
    }

    public Gerant creerGerant(String nom, String adresse, String telephone,
                              double salaire, String motDePasse) throws IOException {
        int num = compteurDAO.prochainGerant();
        String matricule = MatriculeGenerator.generer(Gerant.SUFFIXE, num);
        String hash = PasswordUtil.hash(motDePasse);
        Gerant gerant = new Gerant(matricule, nom, adresse, telephone, salaire, hash);
        employeDAO.sauvegarder(gerant);
        return gerant;
    }
}
