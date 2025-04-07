package geoanalytique.model.geoobject.operation;

import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Cercle;
import geoanalytique.model.Droite;
import geoanalytique.model.Point;
import geoanalytique.util.Operation;

import java.util.ArrayList;
import java.util.List;

/**
 * Opération permettant de calculer les points d'intersection entre un cercle et une droite
 */
public class CalculIntersectionCercleDroiteOperation implements Operation {
    private static final long serialVersionUID = 1L;
    
    private Cercle cercle;
    private Droite droite;
    private GeoAnalytiqueControleur controleur;
    
    /**
     * Constructeur de l'opération
     * @param cercle Le cercle pour lequel on souhaite calculer les intersections
     * @param controleur Le contrôleur de l'application
     */
    public CalculIntersectionCercleDroiteOperation(Cercle cercle, GeoAnalytiqueControleur controleur) {
        this.cercle = cercle;
        this.controleur = controleur;
    }
    
    @Override
    public String getTitle() {
        return "Intersection avec une droite";
    }

    @Override
    public int getArite() {
        return 1; // Nous avons besoin d'un argument: la droite
    }

    @Override
    public void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException {
        if (num == 0) {
            if (o instanceof Droite) {
                this.droite = (Droite) o;
            } else {
                throw new IncorrectTypeOperationException("L'argument doit être une droite");
            }
        } else {
            throw new ArgumentOperationException("Numéro d'argument invalide");
        }
    }

    @Override
    public Class getClassArgument(int num) {
        if (num == 0) {
            return Droite.class;
        }
        return null;
    }

    @Override
    public Object calculer() {
        List<Point> intersections = new ArrayList<>();
        
        // Récupérer les données du cercle
        Point centre = cercle.getCentre();
        double rayon = cercle.getRayon();
        double h = centre.getX();
        double k = centre.getY();
        
        // Cas spécial: droite verticale
        if (Double.isInfinite(droite.getPente())) {
            double x = droite.getPoint().getX();
            
            // Distance du centre à la droite
            double distance = Math.abs(x - h);
            
            // Si la distance est supérieure au rayon, pas d'intersection
            if (distance > rayon) {
                return intersections; // Liste vide
            }
            
            // Si la distance est égale au rayon, tangente (un seul point)
            if (Math.abs(distance - rayon) < Point.DELTA_PRECISION) {
                Point pointIntersection = new Point(x, k, controleur);
                intersections.add(pointIntersection);
                return intersections;
            }
            
            // Deux points d'intersection
            double dy = Math.sqrt(rayon * rayon - distance * distance);
            Point p1 = new Point(x, k + dy, controleur);
            Point p2 = new Point(x, k - dy, controleur);
            intersections.add(p1);
            intersections.add(p2);
            return intersections;
        }
        
        // Équation de la droite: y = mx + b
        double m = droite.getPente();
        double b = droite.calculerOrdonneeOrigine();
        
        // On résout le système d'équations:
        // (x - h)² + (y - k)² = r²  (équation du cercle)
        // y = mx + b            (équation de la droite)
        
        // En substituant y dans l'équation du cercle, on obtient une équation du second degré:
        // (x - h)² + (mx + b - k)² = r²
        // Ax² + Bx + C = 0, où:
        double A = 1 + m * m;
        double B = 2 * (m * b - m * k - h);
        double C = h * h + (b - k) * (b - k) - rayon * rayon;
        
        // Calculer le discriminant
        double discriminant = B * B - 4 * A * C;
        
        // Si le discriminant est négatif, pas d'intersection
        if (discriminant < 0) {
            return intersections; // Liste vide
        }
        
        // Si le discriminant est nul, la droite est tangente au cercle (un seul point)
        if (Math.abs(discriminant) < Point.DELTA_PRECISION) {
            double x = -B / (2 * A);
            double y = m * x + b;
            Point pointIntersection = new Point(x, y, controleur);
            intersections.add(pointIntersection);
            return intersections;
        }
        
        // Deux points d'intersection
        double x1 = (-B + Math.sqrt(discriminant)) / (2 * A);
        double y1 = m * x1 + b;
        Point p1 = new Point(x1, y1, controleur);
        
        double x2 = (-B - Math.sqrt(discriminant)) / (2 * A);
        double y2 = m * x2 + b;
        Point p2 = new Point(x2, y2, controleur);
        
        // Ajouter les points à la liste des intersections
        intersections.add(p1);
        intersections.add(p2);
        
        return intersections;
    }

    @Override
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Entrez le nom de la droite";
        }
        throw new ArgumentOperationException("Numéro d'argument invalide");
    }
} 