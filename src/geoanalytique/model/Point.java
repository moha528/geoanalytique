package geoanalytique.model;

import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
import geoanalytique.model.geoobject.operation.CalculCercleCirconscritOperation;
import geoanalytique.model.geoobject.operation.CalculDistanceOperation;
import geoanalytique.model.geoobject.operation.CalculMilieuOperation;
import geoanalytique.model.geoobject.operation.DeplacerPointOperation;
 
/**
 * Classe représentant les points dans un repère orthogonal
 * Un point est défini par ses coordonnées (x,y)
 */
public class Point extends GeoObject {
    private double x;
    private double y;
    
    // En informatique il est difficile de savoir si deux flottants sont
    // egaux à cause des imprecisions dans les calculs du co-processeur. 
    // Donc pour simplifier nous disons qu'ils sont egaux s'il existe un 
    // delta minuscule (epsilon) entre deux flottants.
    // la valeur choisis a ete prise au hasard
    public static final double DELTA_PRECISION = 0.3;
    
    /**
     * Constructeur d'un point à partir de ses coordonnées
     * @param x Abscisse du point
     * @param y Ordonnée du point
     * @param controleur Contrôleur principal de l'application
     */
    public Point(double x, double y, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.x = x;
        this.y = y;
        initOperations(controleur);
    }
    
    /**
     * Constructeur d'un point avec un nom spécifique
     * @param name Nom du point
     * @param x Abscisse du point
     * @param y Ordonnée du point
     * @param controleur Contrôleur principal de l'application
     */
    public Point(String name, double x, double y, GeoAnalytiqueControleur controleur) {
        super(name, controleur);
        this.x = x;
        this.y = y;
        initOperations(controleur);
    }
    
    /**
     * Initialise les opérations disponibles pour un point
     * @param controleur Contrôleur principal de l'application
     */
    private void initOperations(GeoAnalytiqueControleur controleur) {
        // Ajouter les opérations spécifiques aux points
        getOperations().add(new DeplacerPointOperation(this));
        getOperations().add(new CalculDistanceOperation(this));
        getOperations().add(new CalculMilieuOperation(this, controleur));
        getOperations().add(new CalculCercleCirconscritOperation(this, controleur));
    }

    /**
     * Retourne l'abscisse du point
     * @return Abscisse du point
     */
    public double getX() {
        return x;
    }
    
    /**
     * Retourne l'ordonnée du point
     * @return Ordonnée du point
     */
    public double getY() {
        return y;
    }
    
    /**
     * Modifie l'ordonnée du point
     * @param y Nouvelle ordonnée
     */
    public void setY(double y) {
        this.y = y;
        modifie();
    }
    
    /**
     * Modifie l'abscisse du point
     * @param x Nouvelle abscisse
     */
    public void setX(double x) {
        this.x = x;
        modifie();
    }
    
    /**
     * Calcule la pente entre ce point et un autre point
     * @param a Point avec lequel calculer la pente
     * @return La pente de la droite passant par this et a
     */
    public double calculPente(Point a) {
        if (Math.abs(this.x - a.x) < DELTA_PRECISION) {
            return Double.POSITIVE_INFINITY; // Pente infinie (droite verticale)
        }
        return (this.y - a.y) / (this.x - a.x);
    }
    
    /**
     * Compare ce point avec un autre objet
     * @param o Objet à comparer
     * @return true si l'objet est égal à ce point, false sinon
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        
        Point p = (Point) o;
        return (Math.abs(this.x - p.x) < DELTA_PRECISION) && 
               (Math.abs(this.y - p.y) < DELTA_PRECISION);
    }
   
    /**
     * Calcule la distance euclidienne entre ce point et un autre point
     * @param b Point avec lequel calculer la distance
     * @return Distance entre les deux points
     */
    public double calculerDistance(Point b) {
        return Math.sqrt(Math.pow(this.x - b.x, 2) + Math.pow(this.y - b.y, 2));
    }
    
    /**
     * Déplace le point selon un vecteur (dx, dy)
     * @param dx Déplacement en x
     * @param dy Déplacement en y
     */
    public void deplacer(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        modifie();
    }

    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitPoint(this);
    }

    @Override
    public boolean contient(Point p) {
        return equals(p);
    }
}