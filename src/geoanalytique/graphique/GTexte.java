package geoanalytique.graphique;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/**
 * Graphique permettant d'afficher un texte dans une zone de dessin
 * 
 * @see Graphics#drawString(java.lang.String, int, int) 
 */
public class GTexte extends Graphique {
    private int x;
    private int y;
    private String texte;
    private Font font;
    
    /**
     * Constructeur d'un texte avec une couleur spécifiée
     * @param x Abscisse du point d'origine du texte
     * @param y Ordonnée du point d'origine du texte
     * @param txt Texte à afficher
     * @param color Couleur du texte
     */
    public GTexte(int x, int y, String txt, Color color) {
        this.x = x;
        this.y = y;
        this.texte = txt;
        this.color = color;
        this.font = new Font("SansSerif", Font.PLAIN, 12); // Police par défaut
    }
    
    /**
     * Constructeur d'un texte avec la couleur par défaut (noir)
     * @param x Abscisse du point d'origine du texte
     * @param y Ordonnée du point d'origine du texte
     * @param txt Texte à afficher
     */
    public GTexte(int x, int y, String txt) {
        this(x, y, txt, Color.BLACK);
    }
    
    /**
     * Constructeur d'un texte avec une couleur et une police spécifiées
     * @param x Abscisse du point d'origine du texte
     * @param y Ordonnée du point d'origine du texte
     * @param txt Texte à afficher
     * @param color Couleur du texte
     * @param font Police du texte
     */
    public GTexte(int x, int y, String txt, Color color, Font font) {
        this(x, y, txt, color);
        this.font = font;
    }
     
    /**
     * Définit la police du texte
     * @param font Nouvelle police
     */
    public void setFont(Font font) {
        this.font = font;
    }
    
    /**
     * Retourne la police du texte
     * @return Police du texte
     */
    public Font getFont() {
        return font;
    }
    
    /**
     * Retourne le texte
     * @return Texte
     */
    public String getTexte() {
        return texte;
    }
    
    /**
     * Définit le texte
     * @param texte Nouveau texte
     */
    public void setTexte(String texte) {
        this.texte = texte;
    }
    
    /**
     * Retourne l'abscisse du point d'origine du texte
     * @return x
     */
    public int getX() {
        return x;
    }
    
    /**
     * Retourne l'ordonnée du point d'origine du texte
     * @return y
     */
    public int getY() {
        return y;
    }
    
    /**
     * Dessine le texte sur le contexte graphique
     * @param g Contexte graphique
     * @see Graphics#drawString(java.lang.String, int, int) 
     */
	@Override
	public void paint(Graphics g) {
		Color savedColor = g.getColor();
		Font savedFont = g.getFont();
		
		g.setColor(color);
		g.setFont(font);
		g.drawString(texte, x, y);
		
		g.setColor(savedColor);
		g.setFont(savedFont);
	}
	
	/**
	 * Vérifie si ce texte est égal à un autre objet
	 * @param o Objet à comparer
	 * @return true si les objets sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object o) {
	    if (o == this) return true;
	    if (!(o instanceof GTexte)) return false;
	    
	    GTexte texte = (GTexte) o;
	    return x == texte.x && 
	           y == texte.y && 
	           this.texte.equals(texte.texte);
	}
	
	/**
	 * Retourne une représentation textuelle du texte
	 * @return Chaîne de caractères représentant le texte
	 */
	@Override
	public String toString() {
	    return "Texte: \"" + texte + "\" à (" + x + "," + y + ")";
	}
}
