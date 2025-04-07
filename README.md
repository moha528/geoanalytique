# GéoAnalytique

GéoAnalytique est une application graphique Java permettant de dessiner et manipuler des formes géométriques en utilisant des calculs analytiques. Elle offre la possibilité de créer différents objets géométriques (points, segments, droites, cercles, polygones) et d'effectuer diverses opérations mathématiques sur ces objets.

## Prérequis

- Java JDK 11 ou supérieur
- Eclipse IDE (pour la méthode avec Eclipse) ou un terminal/invite de commandes

## Installation et exécution avec Eclipse

### Méthode 1 : Importer le projet ZIP

1. Téléchargez le code source du projet sous forme d'archive ZIP
2. Ouvrez Eclipse
3. Sélectionnez `File > Import`
4. Choisissez `General > Existing Projects into Workspace`
5. Sélectionnez `Select archive file` et parcourez jusqu'au fichier ZIP téléchargé
6. Cliquez sur `Finish`

### Méthode 2 : Cloner le dépôt Git

1. Ouvrez Eclipse
2. Sélectionnez `File > Import`
3. Choisissez `Git > Projects from Git`
4. Sélectionnez `Clone URI`
5. Entrez l'URI du dépôt Git
6. Suivez les instructions pour compléter le processus de clonage
7. Cliquez sur `Finish`

### Exécution dans Eclipse

1. Dans l'explorateur de packages Eclipse, naviguez jusqu'à `src/geoanalytique/Main.java`
2. Faites un clic droit sur `Main.java`
3. Sélectionnez `Run As > Java Application`

## Installation et exécution sans Eclipse (ligne de commande)

### Téléchargement du code source

#### Option 1 : Télécharger le ZIP

1. Téléchargez le code source du projet sous forme d'archive ZIP
2. Extrayez l'archive dans un dossier de votre choix

#### Option 2 : Cloner avec Git

```bash
git clone [URL-du-dépôt]
cd GeoAnalytique
```

### Compilation et exécution en ligne de commande

1. Ouvrez un terminal ou une invite de commandes
2. Naviguez vers le dossier racine du projet

#### Compilation

```bash
# Créer le dossier bin s'il n'existe pas
mkdir -p bin

# Compiler le projet
javac -d bin -sourcepath src src/geoanalytique/Main.java
```

#### Exécution

```bash
# Exécuter l'application
java -cp bin geoanalytique.Main
```

## Utilisation de base

Une fois l'application lancée, vous pouvez :

1. Créer des objets géométriques (points, segments, droites, cercles, polygones) en utilisant les boutons dans l'interface
2. Sélectionner un objet en cliquant dessus
3. Effectuer diverses opérations sur l'objet sélectionné en choisissant une opération dans la liste et en cliquant sur "Exécuter l'opération"
4. Zoomer, dézoomer et réinitialiser la vue avec les boutons correspondants
5. Sauvegarder l'image du canevas ou le projet entier pour une utilisation ultérieure

## Fonctionnalités

- Création et manipulation d'objets géométriques
- Calcul de propriétés géométriques (distances, intersections, etc.)
- Zoom et navigation dans le plan cartésien
- Sauvegarde et chargement de projets
- Export d'images du canevas

---

Pour plus d'informations, consultez la documentation du projet ou le fichier guide.md. 