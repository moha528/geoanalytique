package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.util.Operation;

/**
 * Classe concrète d'une opération permettant de calculer le milieu
 * entre le point courant et un autre point
 */
public class CalculMilieuOperation implements Operation {

    private Point point1;
    private Point point2;
    private GeoAnalytiqueControleur controleur;
    
    /**
     * Constructeur de l'opération
     * @param point1 Premier point pour le calcul du milieu
     * @param controleur Contrôleur principal de l'application
     */
    public CalculMilieuOperation(Point point1, GeoAnalytiqueControleur controleur) {
        this.point1 = point1;
        this.controleur = controleur;
    }

    /**
     * Calcule le milieu entre les deux points
     * @return Un nouveau Point situé au milieu des deux points
     */
    @Override
    public Object calculer() {
        double xMilieu = (point1.getX() + point2.getX()) / 2;
        double yMilieu = (point1.getY() + point2.getY()) / 2;
        
        // Créer un nouveau point au milieu avec un nom significatif
        String nomMilieu = "M(" + point1.getName() + "," + point2.getName() + ")";
        return new Point(nomMilieu, xMilieu, yMilieu, controleur);
    }

    /**
     * Retourne le nombre d'arguments nécessaires (1: le deuxième point)
     * @return 1
     */
    @Override
    public int getArite() {
        return 1;
    }

    /**
     * Retourne la classe de l'argument demandé
     * @param num Numéro de l'argument (0 pour le deuxième point)
     * @return Point.class
     */
    @Override
    public Class getClassArgument(int num) {
        return Point.class;
    }

    /**
     * Retourne la description de l'argument demandé
     * @param num Numéro de l'argument
     * @return Description de l'argument
     * @throws ArgumentOperationException si le numéro d'argument est invalide
     */
    @Override
    public String getDescriptionArgument(int num) throws ArgumentOperationException {
        if (num == 0) {
            return "Choisir le deuxième point";
        }
        throw new ArgumentOperationException("Argument invalide: " + num);
    }

    /**
     * Retourne le titre de l'opération
     * @return "Calculer le milieu"
     */
    @Override
    public String getTitle() {
        return "Calculer le milieu";
    }

    /**
     * Définit la valeur d'un argument
     * @param num Numéro de l'argument
     * @param o Valeur de l'argument
     * @throws ArgumentOperationException si le numéro d'argument est invalide
     * @throws IncorrectTypeOperationException si le type de l'argument est incorrect
     */
    @Override
    public void setArgument(int num, Object o) 
            throws ArgumentOperationException, IncorrectTypeOperationException {
        if (num != 0) {
            throw new ArgumentOperationException("Argument invalide: " + num);
        }
        
        if (!(o instanceof Point)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Point");
        }
        
        point2 = (Point) o;
    }
} 