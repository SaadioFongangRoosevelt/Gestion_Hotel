# Architecture du Projet - Gestion d'Hôtel (Module Chambre)

Ce document décrit la structure du projet, l'organisation des packages et le rôle de chaque fichier dans le système de gestion d'hôtel.

## 📁 Structure des Dossiers

La structure du projet suit les standards Java, où la hiérarchie des dossiers correspond aux noms des packages.

```text
Gestion_Hotel/
├── bin/                    # Fichiers compilés (.class)
├── src/                    # Code source (.java)
│   └── com/
│       └── hotel/
│           ├── Main.java           # Point d'entrée de l'application
│           ├── dao/                # Data Access Objects (Gestion des données)
│           │   ├── IDao.java       # Interface générique pour les DAO
│           │   └── ChambreDaoTxt.java # Implémentation DAO en fichier texte
│           ├── model/              # Modèles de données (POJO / Héritage)
│           │   ├── Chambre.java    # Classe abstraite de base
│           │   ├── Standard.java   # Type de chambre standard
│           │   ├── VIP.java        # Type de chambre VIP
│           │   ├── Suite.java      # Type de chambre Suite
│           │   ├── TypeChambre.java # Caractéristiques des types (Simple, Double...)
│           │   └── EtatChambreEnum.java # États possibles (DISPO, OCCUPEE...)
│           └── util/               # Utilitaires
│               └── IdGenerator.java # Générateur d'identifiants uniques
├── ARCHITECTURE.md         # Ce fichier de documentation
├── diagramme_classes.html  # Visualisation de la conception
└── README.md               # Présentation rapide du projet
```

---

## 🛠️ Description des Composants

### 1. Package `com.hotel`
*   **`Main.java`** : C'est le point d'entrée. Il orchestre la démonstration du module : initialisation des types, création de chambres via le DAO, simulation de réservations et vérification de disponibilité.

### 2. Package `com.hotel.model` (Le Cœur Métier)
Ce package utilise l'héritage pour gérer les différents types de chambres.
*   **`Chambre.java` (Abstraite)** : Définit les attributs communs (id, numéro, étage, capacité, état) et les méthodes de calcul de prix.
*   **`Standard`, `VIP`, `Suite`** : Sous-classes de `Chambre`. Chaque classe peut redéfinir le calcul du prix (ex: la Suite peut avoir un surcoût fixe ou un multiplicateur).
*   **`TypeChambre.java`** : Classe contenant le libellé et le prix de base (ex: "Simple", "Double", "Deluxe"). Une `Chambre` possède un `TypeChambre`.
*   **`EtatChambreEnum.java`** : Une énumération simple pour sécuriser les états : `DISPONIBLE`, `OCCUPEE`, `EN_NETTOYAGE`, `HORS_SERVICE`.

### 3. Package `com.hotel.dao` (Persistance)
Ce package gère la sauvegarde et la récupération des données.
*   **`IDao.java`** : Une interface générique définissant les opérations CRUD (Create, Read, Update, Delete). Cela permet de changer facilement de mode de stockage (ex: passer de Fichier Texte à SQL) sans modifier le reste du code.
*   **`ChambreDaoTxt.java`** : Implémente `IDao`. Elle stocke les chambres dans une `HashMap` en mémoire et simule l'écriture dans un fichier `chambres.txt`.

### 4. Package `com.hotel.util`
*   **`IdGenerator.java`** : Fournit une logique centralisée pour générer des IDs formatés (ex: `CH-1`, `CH-2`).

---

## 🚀 Fonctionnement de l'Application

Le programme suit un flux logique pour simuler la gestion des chambres :

### 1. Initialisation des Référentiels
Le code commence par définir les types de chambres disponibles (`TypeChambre`). Chaque type possède un prix de base. Ces objets sont ensuite associés aux chambres réelles.

### 2. Gestion de la Persistance (DAO)
L'objet `ChambreDaoTxt` est initialisé. Lors de sa création, il tente de charger les données existantes (simulation). Il sert d'intermédiaire unique pour toute opération sur les chambres (ajout, modification, recherche).

### 3. Création et Enregistrement
Des objets spécifiques (`Standard`, `VIP`, `Suite`) sont instanciés.
- Le `IdGenerator` est utilisé pour garantir que chaque chambre a un identifiant unique (ex: `CH-1`).
- Les chambres sont enregistrées via `chambreDao.sauvegarder()`, ce qui les place en mémoire et simule l'écriture dans `chambres.txt`.

### 4. Affichage et Consultation
Le programme récupère la liste complète des chambres via le DAO et boucle dessus pour afficher leurs détails (Numéro, Type, État, Prix). Grâce au **polymorphisme**, l'appel à `toString()` ou au calcul du prix s'adapte automatiquement au type réel de la chambre (Standard vs Suite).

### 5. Cycle de Vie d'une Chambre
Le code simule un changement d'état (ex: une chambre devient `OCCUPEE`).
- On modifie l'attribut `etat` de l'objet.
- On appelle `chambreDao.modifier()` pour mettre à jour la "base de données" (le fichier texte).

### 6. Logique de Disponibilité
Une méthode `estDispo(dateDebut, dateFin)` permet de vérifier si une chambre peut être louée. Dans cette version, elle vérifie principalement si l'état actuel est `DISPONIBLE`, mais la structure est prête pour intégrer une gestion de calendrier de réservations.

---

## ⚙️ Compilation et Exécution

Pour compiler tout le projet :
```bash
javac -d bin -sourcepath src src/com/hotel/Main.java
```

Pour exécuter l'application :
```bash
java -cp bin com.hotel.Main
```

---

## 💡 Principes de Conception Appliqués
*   **Encapsulation** : Tous les attributs sont privés avec des getters/setters.
*   **Polymorphisme** : Le DAO manipule des objets `Chambre`, qu'ils soient `Standard` ou `VIP`, de manière uniforme.
*   **Séparation des responsabilités (SOC)** : La logique d'affichage (`Main`), la logique métier (`model`) et la logique de stockage (`dao`) sont bien distinctes.
