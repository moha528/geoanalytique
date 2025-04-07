package geoanalytique.model;
 
import geoanalytique.exception.VisiteurException;
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.model.geoobject.operation.CalculPerimetreCercleOperation;

/**
 * Modele mathematique pour les cercles.
 * Un cercle est une ellipse particulière dont le grand axe est égal au petit axe,
 * et cette valeur est le diamètre du cercle.
 */
public class Cercle extends Ellipse {
    private double rayon;
    
    /**
     * Constructeur d'un cercle
     * @param centre Centre du cercle
     * @param rayon Rayon du cercle
     * @param controleur Contrôleur principal de l'application
     */
    public Cercle(Point centre, double rayon, GeoAnalytiqueControleur controleur) {
        super(centre, rayon * 2, rayon * 2, 0, controleur);
        this.rayon = rayon;
        initOperations();
    }
    
    /**
     * Constructeur d'un cercle avec un nom spécifique
     * @param name Nom du cercle
     * @param centre Centre du cercle
     * @param rayon Rayon du cercle
     * @param controleur Contrôleur principal de l'application
     */
    public Cercle(String name, Point centre, double rayon, GeoAnalytiqueControleur controleur) {
        super(name, centre, rayon * 2, rayon * 2, 0, controleur);
        this.rayon = rayon;
        initOperations();
    }
    
    /**
     * Initialise les opérations disponibles pour un cercle
     */
    private void initOperations() {
        // Ajouter l'opération pour calculer le périmètre
        getOperations().add(new CalculPerimetreCercleOperation(this));
    }
    
    /**
     * Retourne le rayon du cercle
     * @return Rayon du cercle
     */
    public double getRayon() {
        return rayon;
    }
    
    /**
     * Calcule le périmètre du cercle
     * @return Périmètre du cercle
     */
    public double calculerPerimetre() {
        return 2 * Math.PI * rayon;
    }
    
    @Override
    public double calculerAire() {
        return Math.PI * rayon * rayon;
    }
    
    @Override
    public boolean contient(Point p) {
        // Un point est dans le cercle si sa distance au centre est inférieure ou égale au rayon
        return p.calculerDistance(getCentre()) <= rayon + Point.DELTA_PRECISION;
    }
    
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitCercle(this);
    }
}
