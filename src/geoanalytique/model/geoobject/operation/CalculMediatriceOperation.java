package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Droite;
import geoanalytique.model.Point;
import geoanalytique.model.Segment;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.util.Operation;

/**
 * Classe concrète d'une opération permettant de calculer la médiatrice d'un segment
 */
public class CalculMediatriceOperation implements Operation {

    private Segment segment;
    private GeoAnalytiqueControleur controleur;
    
    /**
     * Constructeur de l'opération
     * @param segment Segment dont on veut calculer la médiatrice
     * @param controleur Contrôleur principal de l'application
     */
    public CalculMediatriceOperation(Segment segment, GeoAnalytiqueControleur controleur) {
        this.segment = segment;
        this.controleur = controleur;
    }

    /**
     * Calcule la médiatrice du segment
     * @return Une nouvelle Droite représentant la médiatrice
     */
    @Override
    public Object calculer() {
        // Récupère les extrémités du segment
        Point p1 = segment.getPoint1();
        Point p2 = segment.getPoint2();
        
        // Calcule le milieu
        double xMilieu = (p1.getX() + p2.getX()) / 2;
        double yMilieu = (p1.getY() + p2.getY()) / 2;
        Point milieu = new Point(xMilieu, yMilieu, controleur);
        
        // Calcule la pente du segment
        double penteSeg = p1.calculPente(p2);
        double penteMed;
        
        if (Double.isInfinite(penteSeg)) {
            // Si le segment est vertical, sa médiatrice est horizontale
            penteMed = 0;
        } else if (Math.abs(penteSeg) < Point.DELTA_PRECISION) {
            // Si le segment est horizontal, sa médiatrice est verticale
            penteMed = Double.POSITIVE_INFINITY;
        } else {
            // Calcul de la pente perpendiculaire
            penteMed = -1.0 / penteSeg;
        }
        
        // Création de la droite médiatrice
        return new Droite("Med_" + segment.getName(), milieu, penteMed, controleur);
    }

    /**
     * Retourne le nombre d'arguments nécessaires (0: pas d'argument)
     * @return 0
     */
    @Override
    public int getArite() {
        return 0;
    }

    /**
     * Retourne la classe de l'argument demandé
     * @param num Numéro de l'argument
     * @return null car pas d'argument
     */
    @Override
    public Class getClassArgument(int num) {
        return null;
    }

    /**
     * Retourne la description de l'argument demandé
     * @param num Numéro de l'argument
     * @return Description de l'argument
     * @throws ArgumentOperationException toujours car pas d'argument
     */
    @Override
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        throw new ArgumentOperationException("Pas d'argument pour cette opération");
    }

    /**
     * Retourne le titre de l'opération
     * @return "Calculer la médiatrice"
     */
    @Override
    public String getTitle() {
        return "Calculer la médiatrice";
    }

    /**
     * Définit la valeur d'un argument
     * @param num Numéro de l'argument
     * @param o Valeur de l'argument
     * @throws ArgumentOperationException toujours car pas d'argument
     */
    @Override
    public void setArgument(int num, Object o) 
            throws ArgumentOperationException, IncorrectTypeOperationException {
        throw new ArgumentOperationException("Pas d'argument pour cette opération");
    }
} 