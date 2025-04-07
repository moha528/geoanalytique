package geoanalytique.model;
 
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
import geoanalytique.model.geoobject.operation.CalculAirePolygoneOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Classe de base pour les polygones.
 * Un polygone est défini par une liste de points qui forment ses sommets.
 */
public class Polygone extends Surface {
    private List<Point> points;

    /**
     * Constructeur d'un polygone à partir d'une collection de points
     * @param controles Points définissant les sommets du polygone
     * @param controleur Contrôleur principal de l'application
     */
    public Polygone(Collection<Point> controles, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.points = new ArrayList<>(controles);
        initOperations();
    }
    
    /**
     * Constructeur d'un polygone avec un nom spécifique
     * @param name Nom du polygone
     * @param controles Points définissant les sommets du polygone
     * @param controleur Contrôleur principal de l'application
     */
    public Polygone(String name, Collection<Point> controles, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.points = new ArrayList<>(controles);
        initOperations();
    }
    
    /**
     * Initialise les opérations disponibles pour un polygone
     */
    private void initOperations() {
        // Ajouter l'opération pour calculer l'aire
        getOperations().add(new CalculAirePolygoneOperation(this));
    }
    
    /**
     * Retourne la liste des points du polygone
     * @return Liste des points
     */
    public List<Point> getPoints() {
        return new ArrayList<>(points);
    }
    
    /**
     * Retourne le nombre de sommets du polygone
     * @return Nombre de sommets
     */
    public int getNbPoints() {
        return points.size();
    }

    /**
     * Retourne le segment formé par deux sommets consécutifs du polygone
     * @param nb Indice du premier sommet du segment
     * @return Segment reliant les sommets d'indices nb et (nb+1)%getNbPoints()
     */
    public Segment getSegment(int nb) {
        if (nb < 0 || nb >= points.size()) {
            throw new IndexOutOfBoundsException("Indice de segment invalide");
        }
        Point p1 = points.get(nb);
        Point p2 = points.get((nb + 1) % points.size());
        return new Segment(p1, p2, null); // Le contrôleur sera géré par la classe appelante
    }
    
    /**
     * Calcule le périmètre du polygone
     * @return Périmètre du polygone
     */
    public double calculerPerimetre() {
        double perimetre = 0;
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());
            perimetre += p1.calculerDistance(p2);
        }
        return perimetre;
    }
    
    @Override
    public double calculerAire() {
        // Calcul de l'aire par la formule du produit vectoriel (formule de Shoelace)
        double aire = 0;
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size());
            aire += p1.getX() * p2.getY() - p2.getX() * p1.getY();
        }
        return Math.abs(aire) / 2;
    }
    
    @Override
    public Point calculerCentreGravite() {
        // Calcul du centre de gravité comme moyenne des coordonnées des sommets
        double sumX = 0;
        double sumY = 0;
        for (Point p : points) {
            sumX += p.getX();
            sumY += p.getY();
        }
        return new Point(sumX / points.size(), sumY / points.size(), null);
    }
    
    @Override
    public boolean contient(Point p) {
        // Algorithme du point in polygon (ray casting)
        boolean result = false;
        int j = points.size() - 1;
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getY() < p.getY() && points.get(j).getY() >= p.getY() || 
                points.get(j).getY() < p.getY() && points.get(i).getY() >= p.getY()) {
                if (points.get(i).getX() + (p.getY() - points.get(i).getY()) / 
                    (points.get(j).getY() - points.get(i).getY()) * 
                    (points.get(j).getX() - points.get(i).getX()) < p.getX()) {
                    result = !result;
                }
            }
            j = i;
        }
        return result;
    }
    
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitPolygone(this);
    }
}

