package geoanalytique.model;

import geoanalytique.graphique.GCoordonnee;

/**
 * Cette classe definit la zone de dessin virtuel (celle affichable dans le canevas)
 * et les coordonnées reelles sur la vue. 
 * En effet, en mathematique un repere est infini sur l'ensemble des reels, cependant nous ne pouvons afficher
 * qu'une certaine partie dans notre vue. Il nous faut donc un moyen pour convertir
 * les coordonnées reels du repere en coordonnee entiere de la vue.
 *  
 * 
 * 
 * 
 */
public class ViewPort {
    private double xmin;
    private double xmax;
    private double  ymin;
    private double  ymax;
    
    private int largeur;
    private int hauteur;
    private int centreX;
    private int centreY;
    
    public ViewPort(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        centreX = (largeur/2);
        centreY = (hauteur/2);
        xmax = 10;
        xmin = -10;
        ymin = -10;
        ymax = 10;
    }
    
    public void setXMin(double x) {
        xmin = x;
    }
    
    public void setXMax(double x) {
        xmax = x;
    }
    public void setYMin(double y) {
        ymin = y;
    }
    public void setYMax(double y) {
        ymax = y;
    }
    
    
    public int getCentreX() {
    	return centreX;
    }
    
    public int getCentreY() {
    	return centreY;
    }
    
    public void setCentreX(int cx) {
    	centreX = cx;
    }
    
    public void setCentreY(int cy) {
    	centreY = cy;
    }
    
    public int getLargeur() {
    	return largeur;
    }
    
    public int getHauteur() {
    	return hauteur;
    }
    
    public void resize(int largeur,int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
    }
    
    public double getXMin() {return xmin; }
    public double getXMax() {return xmax; }
    public double getYMin() { return ymin; }
    public double getYMax() {return ymax; }
    
    public GCoordonnee convert(double x, double yc) {
    	double y = -yc; // Attention ceci permet de corriger le repere video et graphique...
        int xg = (int)((largeur*(x-xmin))/(xmax-xmin));
        int yg = (int)((hauteur*(y-ymin))/(ymax-ymin));
    	return new GCoordonnee(xg,yg);
    }
    
    public Point convert(int x, int y) {
    	double xm = (((xmax-xmin)*x)/ ((double)largeur))+xmin;
    	double ym = (((ymax-ymin)*y)/ ((double)hauteur))+ymin;
    	return new Point(xm,-ym,null);
    }
}
