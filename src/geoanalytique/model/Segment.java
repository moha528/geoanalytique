package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
 

/**
 *        Un segment est considerer comme une droite car nous passons par au moins 
 *        1 point et que la pente est aussi definit dans le Segment. En revanche 
 *        on pourra lancer une exception si le traitement ne peut s'appliquer sur 
 *        le segment en cours.
 * Un segment est défini par ses deux extrémités.
 * 
 * 
 */
public class Segment extends Droite {
    private Point point1;
    private Point point2;
    
    /**
     * Constructeur d'un segment à partir de deux points
     * @param a Première extrémité du segment
     * @param b Deuxième extrémité du segment
     * @param controleur Contrôleur principal de l'application
     */
    public Segment(Point a, Point b, GeoAnalytiqueControleur controleur) {
        super(a, b, controleur);
        this.point1 = a;
        this.point2 = b;
    }
    
    /**
     * Retourne la première extrémité du segment
     * @return Première extrémité
     */
    public Point getPoint1() {
        return point1;
    }
    
    /**
     * Retourne la deuxième extrémité du segment
     * @return Deuxième extrémité
     */
    public Point getPoint2() {
        return point2;
    }
    
    /**
     * Calcule la longueur du segment
     * @return Longueur du segment
     */
    public double calculerLongueur() {
        return point1.calculerDistance(point2);
    }
    
    /**
     * Calcule le milieu du segment
     * @return Point représentant le milieu du segment
     */
    public Point calculerMilieu(GeoAnalytiqueControleur controleur) {
        double xMilieu = (point1.getX() + point2.getX()) / 2;
        double yMilieu = (point1.getY() + point2.getY()) / 2;
        return new Point(xMilieu, yMilieu, controleur);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Segment)) return false;
        
        Segment s = (Segment) o;
        
        // Deux segments sont égaux s'ils ont les mêmes extrémités (dans n'importe quel ordre)
        return (point1.equals(s.point1) && point2.equals(s.point2)) || 
               (point1.equals(s.point2) && point2.equals(s.point1));
    }
    
    @Override
    public boolean contient(Point p) {
        // Le point doit être sur la droite définie par le segment
        if (!super.contient(p)) {
            return false;
        }
        
        // Vérifier si le point est entre les extrémités du segment
        if (Double.isInfinite(getPente())) {
            // Cas d'un segment vertical
            double minY = Math.min(point1.getY(), point2.getY());
            double maxY = Math.max(point1.getY(), point2.getY());
            return p.getY() >= minY - Point.DELTA_PRECISION && 
                   p.getY() <= maxY + Point.DELTA_PRECISION;
        } else {
            // Cas général
            double minX = Math.min(point1.getX(), point2.getX());
            double maxX = Math.max(point1.getX(), point2.getX());
            return p.getX() >= minX - Point.DELTA_PRECISION && 
                   p.getX() <= maxX + Point.DELTA_PRECISION;
        }
    }
    
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitSegment(this);
    }
}

