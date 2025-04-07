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
    
    /**
     * Creates a viewport with the specified dimensions
     * @param largeur Width of the viewport in pixels
     * @param hauteur Height of the viewport in pixels
     */
    public ViewPort(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.centreX = largeur / 2;
        this.centreY = hauteur / 2;
        
        // Set the initial coordinate range with a consistent aspect ratio
        double aspectRatio = (double) largeur / hauteur;
        
        // Base scale for the viewport (how many units visible in the smaller dimension)
        double baseScale = 20.0;
        
        if (aspectRatio >= 1.0) {
            // Width is larger or equal to height
            xmin = -baseScale * aspectRatio / 2.0;
            xmax = baseScale * aspectRatio / 2.0;
            ymin = -baseScale / 2.0;
            ymax = baseScale / 2.0;
        } else {
            // Height is larger than width
            xmin = -baseScale / 2.0;
            xmax = baseScale / 2.0;
            ymin = -baseScale / aspectRatio / 2.0;
            ymax = baseScale / aspectRatio / 2.0;
        }
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
    
    /**
     * Updates the viewport dimensions while maintaining the mathematical coordinates and aspect ratio
     * @param largeur New width of the viewport
     * @param hauteur New height of the viewport
     */
    public void resize(int largeur, int hauteur) {
        // Store the current visible range
        double rangeX = xmax - xmin;
        double rangeY = ymax - ymin;
        
        // Calculate the current center in mathematical coordinates
        double centerX = (xmax + xmin) / 2.0;
        double centerY = (ymax + ymin) / 2.0;
        
        // Update physical dimensions
        this.largeur = largeur;
        this.hauteur = hauteur;
        this.centreX = largeur / 2;
        this.centreY = hauteur / 2;
        
        // Maintain the aspect ratio based on the larger dimension
        double aspectRatio = (double) largeur / hauteur;
        
        if (aspectRatio >= 1.0) {
            // Width is larger or equal to height
            // Keep X range and adjust Y range
            double newRangeY = rangeX / aspectRatio;
            ymin = centerY - newRangeY / 2.0;
            ymax = centerY + newRangeY / 2.0;
        } else {
            // Height is larger than width
            // Keep Y range and adjust X range
            double newRangeX = rangeY * aspectRatio;
            xmin = centerX - newRangeX / 2.0;
            xmax = centerX + newRangeX / 2.0;
        }
    }
    
    public double getXMin() {return xmin; }
    public double getXMax() {return xmax; }
    public double getYMin() { return ymin; }
    public double getYMax() {return ymax; }
    
    /**
     * Converts mathematical coordinates to screen coordinates
     * @param x X coordinate in the mathematical space
     * @param yc Y coordinate in the mathematical space
     * @return Screen coordinates as a GCoordonnee object
     */
    public GCoordonnee convert(double x, double yc) {
        double y = -yc; // Flip Y-axis to match screen coordinates (positive is down)
        
        // Calculate the proportion of the position within the mathematical range
        double xProportion = (x - xmin) / (xmax - xmin);
        double yProportion = (y - ymin) / (ymax - ymin);
        
        // Convert to screen coordinates
        int xg = (int) Math.round(largeur * xProportion);
        int yg = (int) Math.round(hauteur * yProportion);
        
        return new GCoordonnee(xg, yg);
    }
    
    /**
     * Converts screen coordinates to mathematical coordinates
     * @param x X coordinate on the screen
     * @param y Y coordinate on the screen
     * @return Mathematical coordinates as a Point object
     */
    public Point convert(int x, int y) {
        // Calculate the proportion of the position within the screen
        double xProportion = (double) x / largeur;
        double yProportion = (double) y / hauteur;
        
        // Convert to mathematical coordinates
        double xm = xmin + xProportion * (xmax - xmin);
        double ym = -(ymin + yProportion * (ymax - ymin)); // Flip Y-axis back
        
        return new Point(xm, ym, null);
    }
}
