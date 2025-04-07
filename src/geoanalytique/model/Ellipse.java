package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/** 
 * Modele mathematique pour les ellipses
 * Une ellipse est définie par son centre, son grand axe, son petit axe et son angle d'inclinaison
 */
public class Ellipse extends Surface {
    private Point centre;
    private double grandAxe;
    private double petitAxe;
    private double angle;
    
    /**
     * Constructeur d'une ellipse
     * @param centre Centre de l'ellipse
     * @param grandAxe Longueur du grand axe
     * @param petitAxe Longueur du petit axe
     * @param angle Angle d'inclinaison de l'ellipse (en radians)
     * @param controleur Contrôleur principal de l'application
     */
    public Ellipse(Point centre, double grandAxe, double petitAxe, double angle, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.centre = centre;
        this.grandAxe = grandAxe;
        this.petitAxe = petitAxe;
        this.angle = angle;
    }
    
    /**
     * Constructeur d'une ellipse avec un nom spécifique
     * @param name Nom de l'ellipse
     * @param centre Centre de l'ellipse
     * @param grandAxe Longueur du grand axe
     * @param petitAxe Longueur du petit axe
     * @param angle Angle d'inclinaison de l'ellipse (en radians)
     * @param controleur Contrôleur principal de l'application
     */
    public Ellipse(String name, Point centre, double grandAxe, double petitAxe, double angle, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.centre = centre;
        this.grandAxe = grandAxe;
        this.petitAxe = petitAxe;
        this.angle = angle;
    }
    
    /**
     * Retourne le centre de l'ellipse
     * @return Centre de l'ellipse
     */
    public Point getCentre() {
        return centre;
    }
    
    /**
     * Retourne la longueur du grand axe
     * @return Longueur du grand axe
     */
    public double getGrandAxe() {
        return grandAxe;
    }
    
    /**
     * Retourne la longueur du petit axe
     * @return Longueur du petit axe
     */
    public double getPetitAxe() {
        return petitAxe;
    }
    
    /**
     * Retourne l'angle d'inclinaison de l'ellipse
     * @return Angle d'inclinaison (en radians)
     */
    public double getAngle() {
        return angle;
    }

    @Override
    public double calculerAire() {
        return Math.PI * grandAxe * petitAxe / 4;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ellipse)) return false;
        
        Ellipse e = (Ellipse) o;
        
        return centre.equals(e.centre) &&
               Math.abs(grandAxe - e.grandAxe) < Point.DELTA_PRECISION &&
               Math.abs(petitAxe - e.petitAxe) < Point.DELTA_PRECISION &&
               Math.abs(angle - e.angle) < Point.DELTA_PRECISION;
    }
    
    @Override
    public boolean contient(Point p) {
        // Pour simplifier, on ne considère que le cas d'une ellipse non inclinée
        // Pour une ellipse inclinée, il faudrait effectuer une rotation du point
        if (angle != 0) {
            // Calcul des coordonnées du point après rotation autour du centre de l'ellipse
            double dx = p.getX() - centre.getX();
            double dy = p.getY() - centre.getY();
            double cosAngle = Math.cos(angle);
            double sinAngle = Math.sin(angle);
            double xRot = dx * cosAngle + dy * sinAngle;
            double yRot = -dx * sinAngle + dy * cosAngle;
            
            // Test si le point est dans l'ellipse non inclinée
            return Math.pow(xRot, 2) / Math.pow(grandAxe/2, 2) + 
                   Math.pow(yRot, 2) / Math.pow(petitAxe/2, 2) <= 1;
        } else {
            // Cas d'une ellipse non inclinée
            return Math.pow(p.getX() - centre.getX(), 2) / Math.pow(grandAxe/2, 2) + 
                   Math.pow(p.getY() - centre.getY(), 2) / Math.pow(petitAxe/2, 2) <= 1;
        }
    }
    
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitEllipse(this);
    }

    @Override
    public Point calculerCentreGravite() {
        return centre;
    }
}