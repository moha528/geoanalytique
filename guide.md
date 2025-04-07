
# Contexte du projet "GéoAnalytique"

## 1. Présentation générale
Le projet **"GéoAnalytique"** est un mini-projet de programmation en Java assigné aux étudiants du **Département Génie Informatique (DIC2)** de l’**École Supérieure Polytechnique (ESP)** de l’**Université Cheikh Anta Diop de Dakar (UCAD)** pour l’année universitaire **2024-2025**. L’objectif est de concevoir une **application graphique** permettant de dessiner et manipuler des formes géométriques en utilisant des calculs analytiques. Cette application doit être réalisée par des groupes de **5 à 6 étudiants**, avec une présentation et une livraison prévues pour le **10 avril 2025**.

L’application repose sur un **repère orthogonal** dans lequel les utilisateurs peuvent ajouter des formes géométriques (points, segments, cercles, polygones, etc.) et effectuer des opérations spécifiques à chaque forme, comme calculer une longueur, une pente, ou construire une médiatrice. Chaque étudiant doit être capable d’expliquer l’intégralité du code et les choix techniques réalisés.

## 2. Livrables attendus
Chaque groupe doit fournir :
- Un **document** avec le diagramme de classes demandé.
- Le **code source**, clair et bien documenté (avec commentaires **Javadoc**).
- **OU** une page web donnant accès à la documentation API de toutes les classes.

## 3. Architecture du logiciel
L’application doit être implémentée en suivant une variante du modèle **Model-View-Controller (MVC)**, enrichie d’une entité supplémentaire : le **Présenteur**. Cette architecture est adaptée pour gérer la conversion entre les coordonnées réelles des objets géométriques et les coordonnées entières du canevas graphique. Voici les rôles principaux :

- **Modèle** : Représente les objets géométriques sous forme mathématique (ex. : classe `GeoObject` comme classe abstraite mère, sous-classes comme `Point`, `Segment`, `Cercle`).
- **Présenteur + Contrôleur** : Prépare les coordonnées pour l’affichage, gère les événements de la vue et traduit les interactions utilisateur en modifications du modèle.
- **Vue** : Affiche les figures géométriques sans connaissance directe du modèle, en communiquant uniquement avec le contrôleur.
- Une classe clé, `ViewPort`, permet de convertir les coordonnées réelles (infinies) en coordonnées entières pour l’affichage dans une zone de dessin limitée.

## 4. Squelette fourni
Un squelette de code est fourni, organisé en plusieurs packages :
- `geoanalytique` : Contient la classe `Main` (point d’entrée de l’application).
- `geoanalytique.model` : Définit les objets géométriques (ex. `Point`, `Droite`, `Cercle`) et le sous-package `geoanalytique.model.geoobject.operation` pour les opérations (ex. `ChangeNomOperation`).
- `geoanalytique.controleur` : Inclut `GeoAnalytiqueControleur` (contrôleur principal) et `OperationControleur` (gestion des opérations).
- `geoanalytique.view` : Contient `GeoAnalytiqueView` (zone de dessin, étendant `JPanel`).
- `geoanalytique.graphique` : Définit les objets graphiques (ex. `GLigne`, `GOvale`) héritant de la classe abstraite `Graphique`.
- `geoanalytique.util` : Inclut `Dessinateur` (convertit les modèles en objets graphiques) et l’interface `Operation`.
- `geoanalytique.gui` : Contient `GeoAnalytiqueGUI` (interface graphique principale).
- `geoanalytique.exception` : Gère les exceptions spécifiques (ex. `ArgumentOperationException`).

## 5. Tâches principales
Le projet est divisé en plusieurs parties avec des questions spécifiques à résoudre :

### Modèle
- **Question 1** : Proposer des attributs pour les classes concrètes (ex. `Point` : abscisse et ordonnée ; `Segment` : deux points).
- **Question 2** : Implémenter les classes selon le diagramme UML (figure 2), avec commentaires Javadoc.
- **Question 3** : Ajouter d’autres objets géométriques (ex. triangles, carrés).
- **Question 4** : Implémenter des opérations pour `Point` (déplacer, calculer distance, milieu).
- **Question 5** : Proposer d’autres opérations (ex. médiatrices, surface).

### Vue
- **Question 6** : Concevoir une interface graphique avec une zone de dessin et des éléments comme des boutons ou menus.
- **Question 7** : Compléter les classes du package `geoanalytique.graphique`.

### Contrôleur et Présenteur
- **Question 8** : Compléter `Dessinateur` pour convertir les modèles en objets graphiques.
- **Question 9** : Ajouter les axes Ox et Oy dans `GeoAnalytiqueControleur.recalculPoints`.
- **Question 10** : Implémenter `addObjet`, `selectionner`, et `deselectionner`.
- **Question 11** : Lier le contrôleur et la vue pour une application fonctionnelle.

### Extensions
- **Question 12** : Ajouter des fonctionnalités (ex. zoom, sauvegarde d’image, drag-and-drop).
- **Question 13** : Modifier `lanceOperation` pour permettre plusieurs résultats (ex. intersection cercle-droite).

