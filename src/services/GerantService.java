package services;

import dao.*;
import models.*;
import util.FileConfig;
import util.MatriculeGenerator;
import util.PasswordUtil;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerantService {
    private EmployeDAO employeDAO;
    private ChambreDAO chambreDAO;
    private ClientDAO clientDAO;
    private ReservationDAO reservationDAO;
    private CompteurDAO compteurDAO;

    private static final double BONUS_PAR_QUOTA = 2000;
    private static final int QUOTA_CLIENTS = 10;

    public GerantService() {
        this.employeDAO = new EmployeDAO();
        this.chambreDAO = new ChambreDAO();
        this.clientDAO = new ClientDAO();
        this.reservationDAO = new ReservationDAO();
        this.compteurDAO = new CompteurDAO();
    }

    public Receptionniste recruterReceptionniste(String nom, String adresse,
                                                 String telephone, double salaire,
                                                 String motDePasse) throws IOException {
        int num = compteurDAO.prochainReceptionniste();
        String matricule = MatriculeGenerator.generer(Receptionniste.SUFFIXE, num);
        String hash = PasswordUtil.hash(motDePasse);
        Receptionniste rec = new Receptionniste(matricule, nom, adresse, telephone, salaire, hash, 0);
        employeDAO.sauvegarder(rec);
        return rec;
    }

    /**
     * Paye un réceptionniste : calcule les bonus selon le quota de clients reçus,
     * puis remet le compteur à 0.
     * @return montant total payé (salaire + bonus)
     */
    public double payerReceptionniste(String matricule) throws IOException {
        Employe e = employeDAO.trouverParMatricule(matricule);
        if (!(e instanceof Receptionniste)) return -1;
        Receptionniste rec = (Receptionniste) e;

        int nbClients = rec.getNbClientsRecus();
        int quotasAtteints = nbClients / QUOTA_CLIENTS;
        double bonus = quotasAtteints * BONUS_PAR_QUOTA;
        double totalPaye = rec.getSalaire() + bonus;

        // Remettre le compteur à 0
        rec.setNbClientsRecus(0);
        employeDAO.mettreAJour(rec);

        return totalPaye;
    }

    public boolean mettreChAmbreHorsService(String codeChambre) throws IOException {
        Chambre c = chambreDAO.trouverParCode(codeChambre);
        if (c == null) return false;
        c.setStatut(Chambre.Statut.HORS_SERVICE);
        return chambreDAO.mettreAJour(c);
    }

    public boolean remettreEnService(String codeChambre) throws IOException {
        Chambre c = chambreDAO.trouverParCode(codeChambre);
        if (c == null) return false;
        c.setStatut(Chambre.Statut.LIBRE);
        return chambreDAO.mettreAJour(c);
    }

    public Chambre ajouterChambre(Chambre.Standing standing, double prix) throws IOException {
        int num = compteurDAO.prochainChambre();
        String code = MatriculeGenerator.genererCodeChambre(num);
        Chambre chambre = new Chambre(code, standing, Chambre.Statut.LIBRE, prix);
        chambreDAO.sauvegarder(chambre);
        return chambre;
    }

    // ---- Statistiques ----

    public Map<String, Object> getStatistiques() throws IOException {
        Map<String, Object> stats = new HashMap<>();
        List<Client> clients = clientDAO.lireTous();
        List<Receptionniste> recs = employeDAO.lireReceptionnistes();
        List<Chambre> chambres = chambreDAO.lireTous();

        stats.put("nbClients", clients.size());
        stats.put("clients", clients);
        stats.put("nbReceptionnistes", recs.size());
        stats.put("receptionnistes", recs);
        stats.put("nbChambres", chambres.size());
        stats.put("chambres", chambres);

        long standard    = chambres.stream().filter(c -> c.getStanding() == Chambre.Standing.STANDARD).count();
        long vip         = chambres.stream().filter(c -> c.getStanding() == Chambre.Standing.VIP).count();
        long suite       = chambres.stream().filter(c -> c.getStanding() == Chambre.Standing.SUITE).count();
        long occupees    = chambres.stream().filter(c -> c.getStatut() == Chambre.Statut.OCCUPEE).count();
        long libres      = chambres.stream().filter(c -> c.getStatut() == Chambre.Statut.LIBRE).count();
        long horsService = chambres.stream().filter(c -> c.getStatut() == Chambre.Statut.HORS_SERVICE).count();

        stats.put("nbStandard", standard);
        stats.put("nbVip", vip);
        stats.put("nbSuite", suite);
        stats.put("nbOccupees", occupees);
        stats.put("nbLibres", libres);
        stats.put("nbHorsService", horsService);

        return stats;
    }

    public List<Receptionniste> listerReceptionnistes() throws IOException {
        return employeDAO.lireReceptionnistes();
    }

    public List<Chambre> listerChambres() throws IOException {
        return chambreDAO.lireTous();
    }

    public Chambre trouverChambre(String code) throws IOException {
        return chambreDAO.trouverParCode(code);
    }

    public Receptionniste trouverReceptionniste(String matricule) throws IOException {
        Employe e = employeDAO.trouverParMatricule(matricule);
        return (e instanceof Receptionniste) ? (Receptionniste) e : null;
    }

    public boolean licencierReceptionniste(String matricule) throws IOException {
        Employe e = employeDAO.trouverParMatricule(matricule);
        if (!(e instanceof Receptionniste)) return false;

        List <Employe> liste = employeDAO.lireTous();
        boolean retire = liste.removeIf(emp -> emp.getMatricule().equalsIgnoreCase(matricule));
        if (retire)
        {
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(FileConfig.EMPLOYES_FILE, false))){
                for (Employe emp : liste) {
                    bw.write(emp.toString());
                    bw.newLine();
                }
            }
        }
        return retire;
    }
}

