package geoanalytique.graphique;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Classe de base pour les objets graphiques pouvant etre 'retenu' par la vue.
 * Ces objets sont identifies par des caracteristiques visuelles comme la couleur.
 * Par ailleurs elles savent se dessiner a l'aide d'un objet Graphics de Java.
 * 
 * @see java.awt.Graphics 
 * 
 */
public abstract class Graphique {

	protected Color color;
	
	public abstract void paint(Graphics g);

	public void setCouleur(Color red) {
		this.color = red;		
	}
	
	public Color getCouleur() {
		return color;
	}
}
