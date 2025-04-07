package geoanalytique.graphique;

import java.awt.Color;
import java.awt.Graphics;
 
/**
 * Objet permettant de tracer des lignes sur le canevas.
 * 
 */
public class GLigne extends Graphique {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    
    /**
     * Constructeur d'une ligne avec la couleur par défaut (noir)
     * @param x1 Abscisse du premier point
     * @param y1 Ordonnée du premier point
     * @param x2 Abscisse du deuxième point
     * @param y2 Ordonnée du deuxième point
     */
    public GLigne(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = Color.BLACK;
    }
    
    /**
     * Constructeur d'une ligne avec une couleur spécifiée
     * @param x1 Abscisse du premier point
     * @param y1 Ordonnée du premier point
     * @param x2 Abscisse du deuxième point
     * @param y2 Ordonnée du deuxième point
     * @param color Couleur de la ligne
     */
    public GLigne(int x1, int y1, int x2, int y2, Color color) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
    }
    
    /**
     * Vérifie si cette ligne est égale à un autre objet
     * @param o Objet à comparer
     * @return true si les objets sont égaux, false sinon
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof GLigne)) return false;
        
        GLigne ligne = (GLigne) o;
        // Deux lignes sont égales si elles ont les mêmes points (dans n'importe quel ordre)
        return (x1 == ligne.x1 && y1 == ligne.y1 && x2 == ligne.x2 && y2 == ligne.y2) ||
               (x1 == ligne.x2 && y1 == ligne.y2 && x2 == ligne.x1 && y2 == ligne.y1);
    }
    
    /**
     * Définit la couleur de la ligne
     * @param c Nouvelle couleur
     */
    @Override
    public void setCouleur(Color c) {
    	this.color = c;
    }

    /**
     * Dessine la ligne sur le contexte graphique
     * @param g Contexte graphique
     */
	@Override
	public void paint(Graphics g) {
		Color savedColor = g.getColor();
		g.setColor(color);
		g.drawLine(x1, y1, x2, y2);
		g.setColor(savedColor);
	}
	
	/**
	 * Retourne une représentation textuelle de la ligne
	 * @return Chaîne de caractères représentant la ligne
	 */
	public String toString() {
		return "Ligne: (" + x1 + "," + y1 + ") - (" + x2 + "," + y2 + ")";
	}
	
	/**
	 * Retourne l'abscisse du premier point
	 * @return x1
	 */
	public int getX1() {
	    return x1;
	}
	
	/**
	 * Retourne l'ordonnée du premier point
	 * @return y1
	 */
	public int getY1() {
	    return y1;
	}
	
	/**
	 * Retourne l'abscisse du deuxième point
	 * @return x2
	 */
	public int getX2() {
	    return x2;
	}
	
	/**
	 * Retourne l'ordonnée du deuxième point
	 * @return y2
	 */
	public int getY2() {
	    return y2;
	}
}
