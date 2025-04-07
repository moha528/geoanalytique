package geoanalytique.graphique;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe composite permettant de gérer plusieurs graphiques comme un seul objet.
 * Utile pour dessiner des formes complexes composées de plusieurs éléments.
 */
public class GraphiqueComposite extends Graphique {
    private List<Graphique> graphiques;

    /**
     * Constructeur de la classe composite
     */
    public GraphiqueComposite() {
        this.graphiques = new ArrayList<>();
    }

    /**
     * Ajoute un graphique à la composition
     * @param graphique Le graphique à ajouter
     */
    public void addGraphique(Graphique graphique) {
        graphiques.add(graphique);
    }

    /**
     * Dessine tous les graphiques de la composition
     * @param g Le contexte graphique
     */
    @Override
    public void paint(Graphics g) {
        for (Graphique graphique : graphiques) {
            graphique.paint(g);
        }
    }

    /**
     * Définit la couleur de tous les graphiques de la composition
     * @param c La nouvelle couleur
     */
    @Override
    public void setCouleur(java.awt.Color c) {
        for (Graphique graphique : graphiques) {
            graphique.setCouleur(c);
        }
    }
} 