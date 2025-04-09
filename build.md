# Guide de Compilation et d'Exécution de GéoAnalytique

## Prérequis
- Java Development Kit (JDK) 8 ou supérieur
- Eclipse IDE (recommandé) ou un autre IDE Java
- JUnit 3 pour les tests unitaires

## Structure du Projet
```
GeoAnalytique/
├── src/               # Code source
├── test/              # Tests unitaires
├── bin/               # Fichiers compilés
├── doc/               # Documentation
└── .classpath         # Configuration Eclipse
```

## Méthode 1 : Compilation avec Eclipse

1. **Importer le projet**
   - Ouvrir Eclipse
   - File > Import > General > Existing Projects into Workspace
   - Sélectionner le dossier racine du projet
   - Cliquer sur "Finish"

2. **Compiler le projet**
   - Project > Clean...
   - Sélectionner le projet GéoAnalytique
   - Cliquer sur "OK"
   - Le projet sera automatiquement compilé

3. **Exécuter l'application**
   - Ouvrir `src/geoanalytique/Main.java`
   - Run > Run As > Java Application

4. **Exécuter les tests**
   - Ouvrir le dossier `test`
   - Run > Run As > JUnit Test

## Méthode 2 : Compilation en ligne de commande

1. **Créer le dossier de sortie**
   ```bash
   mkdir -p bin
   ```

2. **Compiler les sources**
   ```bash
   javac -d bin -sourcepath src src/geoanalytique/Main.java
   ```

3. **Exécuter l'application**
   ```bash
   java -cp bin geoanalytique.Main
   ```

4. **Compiler et exécuter les tests**
   ```bash
   # Compiler les tests
   javac -d bin -cp "bin:lib/junit.jar" test/**/*.java
   
   # Exécuter les tests
   java -cp "bin:lib/junit.jar" org.junit.runner.JUnitCore geoanalytique.test.AllTests
   ```

## Méthode 3 : Création d'un JAR exécutable

1. **Compiler le projet**
   ```bash
   javac -d bin -sourcepath src src/geoanalytique/Main.java
   ```

2. **Créer le JAR**
   ```bash
   jar cvfe GeoAnalytique.jar geoanalytique.Main -C bin .
   ```

3. **Exécuter le JAR**
   ```bash
   java -jar GeoAnalytique.jar
   ```

## Résolution des problèmes courants

### Erreur de compilation
- Vérifier que le JDK est correctement installé et configuré
- Vérifier que le JAVA_HOME est correctement défini
- Vérifier que toutes les dépendances sont présentes

### Erreur d'exécution
- Vérifier que le classpath inclut tous les dossiers et bibliothèques nécessaires
- Vérifier les droits d'accès aux fichiers
- Consulter les logs d'erreur dans la console

### Erreur de tests
- Vérifier que JUnit est correctement configuré
- Vérifier que tous les tests sont dans le bon package
- Vérifier que les chemins de test sont corrects

## Support

Pour toute question ou problème :
1. Consulter la documentation dans le dossier `doc/`
2. Vérifier les issues connues dans le système de suivi
3. Contacter l'équipe de développement

---

*Note : Ces instructions supposent un environnement Unix-like (Linux/MacOS). Pour Windows, ajustez les chemins en utilisant des backslashes (\) et adaptez les commandes en conséquence.* 