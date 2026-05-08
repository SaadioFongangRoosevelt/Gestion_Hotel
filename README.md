# 🏨 Système de Gestion Hôtelière

> Application console Java — Programmation Orientée Objet
---

## 1. Présentation

Ce projet est une application console Java de gestion hôtelière, développée dans le cadre de l'unité Programmation Orientée Objet (POO). Il modélise la gestion des employés, des clients, des chambres et des réservations d'un hôtel, avec une interface entièrement en ligne de commande.

Le système distingue deux types d'utilisateurs avec des rôles et menus distincts :
**• Le Gérant:** 
    accès complet à l'administration (recrutement, paiement, statistiques, gestion des chambres)
**• Le Réceptionniste :** 
    gestion des clients et des réservations

L'application est entièrement persistante via des fichiers texte (.txt) à champs séparés par des tabulations.

---

## 2. Structure du projet

```
.
├── src/
│   ├── Main.java                    ← Point d'entrée (méthode main)
│   ├── models/
│   │   ├── Personne.java            ← Classe abstraite de base
│   │   ├── Employe.java             ← Hérite de Personne
│   │   ├── Gerant.java              ← Suffixe GER, unique dans le système
│   │   ├── Receptionniste.java      ← Suffixe REC, compteur clients
│   │   ├── Client.java              ← Suffixe CLI
│   │   ├── Chambre.java             ← Enums Standing & Statut
│   │   └── Reservation.java         ← Avec LocalDate
│   ├── dao/
│   │   ├── EmployeDAO.java
│   │   ├── ClientDAO.java
│   │   ├── ChambreDAO.java
│   │   ├── ReservationDAO.java
│   │   └── CompteurDAO.java         ← Auto-incréments des codes
│   ├── services/
│   │   ├── InitService.java         ← Logique de premier lancement
│   │   ├── AuthService.java         ← Authentification
│   │   ├── ReceptionnisteService.java
│   │   └── GerantService.java
│   ├── ui/
│   │   ├── ReceptionnisteUI.java
│   │   └── GerantUI.java
│   └── util/
│       ├── ConsoleUtil.java         ← Couleurs ANSI, boîtes, tableaux
│       ├── PasswordUtil.java        ← SHA-256 + saisie masquée (*)
│       ├── MatriculeGenerator.java  ← Génération GER001, REC001...
│       └── FileConfig.java          ← Chemins centralisés des fichiers
└── data/
    ├── employes.txt
    ├── clients.txt
    ├── chambres.txt
    ├── reservations.txt
    └── compteurs.txt
```

| Dossier | Rôle |
|---|---|
| `src/models/` | Entités métier |
| `src/dao/` | Accès aux fichiers de données |
| `src/services/` | Logique métier |
| `src/ui/` | Affichage et menus console |
| `src/util/` | Utilitaires transversaux |
| `data/` | Fichiers de persistance |

---

## 3. Compilation et lancement

### Prérequis

- Java JDK recent de preference.
- Terminal Linux ou macOS (pour la masquer la saisie du mot de passe)

### Compiler

Depuis la racine du projet (là où se trouve `src/`) :
```bash
javac Main.java
```

### Lancer

```bash
java  Main
```
ou tout simplement taper 
```bash 
java Main.java
```

> **Note :** le dossier `data/` est créé automatiquement au premier lancement. Il doit se trouver dans le répertoire depuis lequel la commande `java` est exécutée.

---

## 4. Premier lancement

Au tout premier démarrage, le programme détecte qu'aucun gérant n'existe dans les fichiers de sauvegarde et lance la création interactive :

1. Saisie du nom, adresse, téléphone, salaire et mot de passe
2. Le matricule `GER001` est attribué automatiquement
3. Le mot de passe est haché en SHA-256 avant d'être enregistré dans `employes.txt`

Tant qu'aucun réceptionniste n'est enregistré, le système **force la connexion en mode gérant** afin d'obliger la création d'au moins un réceptionniste avant toute autre opération.

---

## 5. Fonctionnalités

### Menu Gérant

| Option | Description |
|---|---|
| `[1]` Recruter un réceptionniste | Crée un compte avec matricule auto (`REC001`, `REC002`...) |
| `[2]` Payer un réceptionniste | Calcule salaire + bonus, remet le compteur à 0 |
| `[3]` Ajouter une chambre | Crée une chambre STANDARD / VIP / SUITE avec code auto |
| `[4]` Mettre une chambre hors service | Passe le statut à `HORS_SERVICE` |
| `[5]` Remettre une chambre en service | Repasse le statut à `LIBRE` |
| `[6]` Statistiques générales | Clients, réceptionnistes, chambres par type et par statut |
| `[7]` Liste des réceptionnistes | Matricule, nom, salaire, clients reçus |
| `[8]` Liste des chambres | Tableau complet de toutes les chambres |
| `[9]` Licencier un réceptionniste | Supprime définitivement le réceptionniste |
| `[0]` Déconnexion | Retour à l'écran de connexion |

### Menu Réceptionniste

| Option | Description |
|---|---|
| `[1]` Effectuer une réservation | Affiche les chambres libres, crée le client et la réservation, passe la chambre en `OCCUPEE` |
| `[2]` Supprimer un client | Supprime le client et sa réservation, remet la chambre à `LIBRE` |
| `[3]` Modifier un client | Modifie le nom, l'adresse ou le téléphone |
| `[4]` Afficher les clients | Liste tous les clients enregistrés |
| `[5]` Rechercher un client | Recherche par code client (`CLI001`...) |
| `[6]` Chambres libres | Affiche toutes les chambres disponibles |
| `[0]` Déconnexion | Retour à l'écran de connexion |

---

## 6. Règles métier

### Calcul du montant d'une réservation

```
Montant = nombreJours × (prix de la chambre + service supplémentaire)
```

| Standing | Service supplémentaire |
|---|---|
| STANDARD | 2 000 F |
| VIP | 5 000 F |
| SUITE | 10 000 F |

### Bonus réceptionniste

- Chaque réservation incrémente le compteur `nbClientsRecus` du réceptionniste
- Au moment du paiement : `bonus = (nbClientsRecus ÷ 10) × 2 000 F`
- Exemple : 23 clients → 2 quotas atteints → **4 000 F de bonus**
- Le compteur est remis à **0** après le paiement

### Statuts des chambres

| Transition | Qui peut l'effectuer | Déclencheur |
|---|---|---|
| `LIBRE` → `OCCUPEE` | Réceptionniste | Lors d'une réservation |
| `OCCUPEE` → `LIBRE` | Réceptionniste | Suppression du client associé |
| `LIBRE` → `HORS_SERVICE` | Gérant uniquement | Option `[4]` |
| `HORS_SERVICE` → `LIBRE` | Gérant uniquement | Option `[5]` |

### Contraintes

- Impossible de réserver s'il n'y a aucune chambre enregistrée
- Impossible de réserver s'il n'y a aucun réceptionniste enregistré
- Un client est enregistre lors d'une reservation (bien sur qu'on est client si l'on achete la marchandise)
- Sélectionner une chambre `HORS_SERVICE` affiche une erreur et redemande
- gestion des exception
- Un seul et unique gérant peut exister dans le système

---

## 7. Fichiers de données

Tous les fichiers sont dans `data/` et utilisent la **tabulation** (`\t`) comme séparateur.

### `compteurs.txt`

Fichier Properties Java gérant les auto-incréments :

```properties
gerant=1
receptionniste=3
client=12
reservation=10
chambre=5
```

---

## 8. Sécurité

### Hachage des mots de passe

Les mots de passe sont hachés via `java.security.MessageDigest` (SHA-256) avant tout stockage :

```java
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] hash = md.digest(password.getBytes());
// Stocké en hexadécimal dans employes.txt
```

### Saisie masquée

Le mot de passe ne s'affiche jamais à l'écran. Chaque caractère tapé est immédiatement remplacé par `*`. Deux modes :

| Mode | Mécanisme |
|---|---|
| Terminal natif (`System.console()` disponible) | `console.readPassword()` — masquage natif Java |
| Sans console (IDE, redirection...) | Lecture directe sur `/dev/tty` en mode raw (`stty -echo -icanon min 1`), chaque caractère remplacé par `*` à la frappe |

- Le **backspace** efface le dernier `*` et le dernier caractère saisi
- **Ctrl+C** annule proprement la saisie
- L'écho du terminal est toujours restauré dans le `finally`, même en cas d'erreur

cette fonctionnalite n'est pas encore parfaitement fonctionnelle seul le Terminal natif fonctionne.
---

## 9. Interface console

### Code couleur ANSI
 Les couleurs ont ete utilise dans le but d'embellir le l'interface console
 
| Couleur | Utilisation |
|---|---|
| Cyan | Titres, bordures, numéros d'options |
| Vert | Messages de succès ✔ |
| Rouge | Messages d'erreur ✘ |
| Violet | Chiffres dans les statistiques |
| Jaune | Messages d'information ℹ |

---
## 10. Dépendances

Aucune bibliothèque externe. Uniquement la bibliothèque standard Java (JDK 11+) :

| Package Java | Utilisation |
|---|---|
| `java.io` | `BufferedReader`, `BufferedWriter`, `FileReader`, `FileWriter`, `Console`, `FileInputStream` |
| `java.security` | `MessageDigest` — hachage SHA-256 |
| `java.time` | `LocalDate` — date de réservation |
| `java.util` | `List`, `ArrayList`, `Map`, `HashMap`, `Properties`, `Scanner` |