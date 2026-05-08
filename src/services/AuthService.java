package services;

import dao.EmployeDAO;
import models.Employe;
import util.PasswordUtil;

import java.io.IOException;

public class AuthService {
    private EmployeDAO employeDAO;

    public AuthService() {
        this.employeDAO = new EmployeDAO();
    }
    //Authentication via mot de passe et matricule
    public Employe authentifier(String matricule, String motDePasse) throws IOException {
        Employe employe = employeDAO.trouverParMatricule(matricule);
        if (employe == null) return null;
        if (PasswordUtil.verify(motDePasse, employe.getMotDePasseHash())) {
            return employe;
        }
        return null;
    }
}
