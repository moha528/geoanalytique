package geoanalytique.graphique;
 
import java.awt.Color;
import java.awt.Graphics;

/**
 * Objet graphique permettant de dessiner un ovale quelconque a la Java
 * (ellipse ou cercle).
 * 
 * 
 * @see java.awt.Graphics#drawOval(int, int, int, int) 
 */
public class GOvale extends Graphique {
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean filled; // Pour indiquer si l'ovale est rempli ou non
	
	/**
	 * Constructeur d'un ovale
	 * @param x Abscisse du coin supérieur gauche du rectangle englobant
	 * @param y Ordonnée du coin supérieur gauche du rectangle englobant
	 * @param w Largeur du rectangle englobant (diamètre horizontal pour un cercle)
	 * @param h Hauteur du rectangle englobant (diamètre vertical pour un cercle)
	 * @param color Couleur de l'ovale
	 */
	public GOvale(int x, int y, int w, int h, Color color) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.color = color;
		this.filled = false; // Par défaut, l'ovale n'est pas rempli
	}
	
	/**
	 * Constructeur d'un ovale avec option de remplissage
	 * @param x Abscisse du coin supérieur gauche du rectangle englobant
	 * @param y Ordonnée du coin supérieur gauche du rectangle englobant
	 * @param w Largeur du rectangle englobant (diamètre horizontal pour un cercle)
	 * @param h Hauteur du rectangle englobant (diamètre vertical pour un cercle)
	 * @param color Couleur de l'ovale
	 * @param filled true si l'ovale doit être rempli, false sinon
	 */
	public GOvale(int x, int y, int w, int h, Color color, boolean filled) {
		this(x, y, w, h, color);
		this.filled = filled;
	}

	/**
	 * Dessine l'ovale sur le contexte graphique
	 * @param g Contexte graphique
	 * @see geoanalytique.graphique.Graphique#paint(java.awt.Graphics)
     * @see java.awt.Graphics#drawOval(int, int, int, int) 
	 */
	@Override
	public void paint(Graphics g) {
		Color savedColor = g.getColor();
		g.setColor(color);
		
		if (filled) {
			g.fillOval(x, y, width, height);
		} else {
			g.drawOval(x, y, width, height);
		}
		
		g.setColor(savedColor);
	}
	
	/**
	 * Définit si l'ovale est rempli ou non
	 * @param filled true si l'ovale doit être rempli, false sinon
	 */
	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	
	/**
	 * Vérifie si l'ovale est rempli
	 * @return true si l'ovale est rempli, false sinon
	 */
	public boolean isFilled() {
		return filled;
	}
	
	/**
	 * Retourne l'abscisse du coin supérieur gauche du rectangle englobant
	 * @return x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Retourne l'ordonnée du coin supérieur gauche du rectangle englobant
	 * @return y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Retourne la largeur du rectangle englobant
	 * @return width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Retourne la hauteur du rectangle englobant
	 * @return height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Vérifie si cet ovale est égal à un autre objet
	 * @param o Objet à comparer
	 * @return true si les objets sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof GOvale)) return false;
		
		GOvale ovale = (GOvale) o;
		return x == ovale.x && 
			   y == ovale.y && 
			   width == ovale.width && 
			   height == ovale.height && 
			   filled == ovale.filled;
	}
	
	/**
	 * Retourne une représentation textuelle de l'ovale
	 * @return Chaîne de caractères représentant l'ovale
	 */
	@Override
	public String toString() {
		String type = (width == height) ? "Cercle" : "Ellipse";
		return type + ": position=(" + x + "," + y + ") taille=(" + width + "x" + height + ")";
	}
}
