package geoanalytique;
 
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.gui.GeoAnalytiqueGUI;

import geoanalytique.model.Point;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Classe de lancement principale de l'application.
 * Cette classe initialise l'interface graphique et le contrôleur
 * 
 */
public class Main {

    /**
     * Méthode principale de l'application
     * @param args arguments de la ligne de commande (non utilisés)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    // Utiliser le look and feel natif du système
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Impossible de définir le look and feel: " + e.getMessage());
                }
                
                // Création de l'interface graphique
                GeoAnalytiqueGUI panel = new GeoAnalytiqueGUI();
                
                // Création de la fenêtre principale
                JFrame frame = new JFrame("GéoAnalytique - Manipulations géométriques");
                frame.getContentPane().add(panel);
                frame.setSize(900, 700); // Plus grande taille pour notre interface enrichie
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null); // Centrer la fenêtre
                
                // Création du contrôleur et initialisation
                GeoAnalytiqueControleur controleur = new GeoAnalytiqueControleur(panel);
                controleur.prepareTout();
                
                // Ajouter quelques objets de démonstration
                controleur.addObjet(new Point("O", 0, 0, controleur));
                controleur.addObjet(new Point("A", 5, 5, controleur));
                controleur.addObjet(new Point("B", -3, 4, controleur));
                
                // Afficher la fenêtre
                frame.setVisible(true);
            }
        });
    }
}
