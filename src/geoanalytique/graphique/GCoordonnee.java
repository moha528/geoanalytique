package geoanalytique.graphique;
 
import java.awt.Color;
import java.awt.Graphics;

/**
 * Objet graphique representant un point sur la vue. Pour des raisons
 * de visibilite un point sera vue comme un petit disque sur le canevas.
 * 
 */
public class GCoordonnee extends Graphique {
	private final int x;
	private final int y;
        
        // Cette variable indique uniquement la taille pour l'affichage d'un point
        // En effet, visualiser un pixel sur un ecran est parfois difficile
        // on représentera donc un point par un cercle de diametre TAILLE_POINT
	public static final int TAILLE_POINT = 9;

	public GCoordonnee(int x, int y) {
		this. x = x;
		this.y = y;
		color = Color.BLUE;
	}
	
	public GCoordonnee(int x, int y, Color color) {
		this. x = x;
		this.y = y;
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	

	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof GCoordonnee) {
			GCoordonnee co = (GCoordonnee)o;
			return co.getX() == x && co.getY() == y;
		}
		return false;
	}

	@Override
	public void paint(Graphics g) {
		Color save = g.getColor();
		g.setColor(color);
		g.fillOval(x-(TAILLE_POINT/2), y-(TAILLE_POINT/2), TAILLE_POINT, TAILLE_POINT);
		g.setColor(save);
	}
	
	public String toString() {
		return "Coordonnee: x="+x+" y="+y;
	}
}
