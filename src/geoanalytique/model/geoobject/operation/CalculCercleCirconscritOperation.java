package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Cercle;
import geoanalytique.model.Droite;
import geoanalytique.model.Point;
import geoanalytique.model.Segment;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.util.Operation;

/**
 * Classe concrète d'une opération permettant de calculer le cercle circonscrit à un triangle
 * défini par trois points
 */
public class CalculCercleCirconscritOperation implements Operation {

    private Point pointA;
    private Point pointB;
    private Point pointC;
    private GeoAnalytiqueControleur controleur;
    
    /**
     * Constructeur de l'opération
     * @param pointA Premier point du triangle
     * @param controleur Contrôleur principal de l'application
     */
    public CalculCercleCirconscritOperation(Point pointA, GeoAnalytiqueControleur controleur) {
        this.pointA = pointA;
        this.controleur = controleur;
    }

    /**
     * Calcule le cercle circonscrit au triangle ABC
     * @return Un nouveau Cercle représentant le cercle circonscrit
     */
    @Override
    public Object calculer() {
        // Vérification que les points ne sont pas alignés
        double aire = Math.abs(
            (pointA.getX() * (pointB.getY() - pointC.getY()) +
             pointB.getX() * (pointC.getY() - pointA.getY()) +
             pointC.getX() * (pointA.getY() - pointB.getY())) / 2
        );
        
        if (aire < Point.DELTA_PRECISION) {
            return "Les trois points sont alignés, pas de cercle circonscrit";
        }
        
        // Création des segments
        Segment AB = new Segment(pointA, pointB, controleur);
        Segment BC = new Segment(pointB, pointC, controleur);
        
        // Calcul des médiatrices
        Point milieuAB = new Point((pointA.getX() + pointB.getX()) / 2, 
                                    (pointA.getY() + pointB.getY()) / 2, 
                                    controleur);
        Point milieuBC = new Point((pointB.getX() + pointC.getX()) / 2, 
                                    (pointB.getY() + pointC.getY()) / 2, 
                                    controleur);
        
        // Pentes des médiatrices
        double penteAB = AB.getPente();
        double penteBC = BC.getPente();
        double penteMedAB = Double.isInfinite(penteAB) ? 0 : -1.0 / penteAB;
        double penteMedBC = Double.isInfinite(penteBC) ? 0 : -1.0 / penteBC;
        
        // Création des équations des médiatrices
        // Équation forme y = ax + b
        double b1 = milieuAB.getY() - penteMedAB * milieuAB.getX();
        double b2 = milieuBC.getY() - penteMedBC * milieuBC.getX();
        
        // Calcul du centre du cercle circonscrit (intersection des médiatrices)
        Point centre;
        
        if (Double.isInfinite(penteMedAB)) {
            // Première médiatrice verticale
            double x = milieuAB.getX();
            double y = penteMedBC * x + b2;
            centre = new Point(x, y, controleur);
        } else if (Double.isInfinite(penteMedBC)) {
            // Deuxième médiatrice verticale
            double x = milieuBC.getX();
            double y = penteMedAB * x + b1;
            centre = new Point(x, y, controleur);
        } else if (Math.abs(penteMedAB - penteMedBC) < Point.DELTA_PRECISION) {
            // Médiatrices parallèles
            return "Impossible de construire le cercle circonscrit";
        } else {
            // Cas général
            double x = (b2 - b1) / (penteMedAB - penteMedBC);
            double y = penteMedAB * x + b1;
            centre = new Point(x, y, controleur);
        }
        
        // Calcul du rayon
        double rayon = centre.calculerDistance(pointA);
        
        // Création du cercle circonscrit
        return new Cercle("Circ(" + pointA.getName() + pointB.getName() + pointC.getName() + ")",
                           centre, rayon, controleur);
    }

    /**
     * Retourne le nombre d'arguments nécessaires (2: les deux autres points)
     * @return 2
     */
    @Override
    public int getArite() {
        return 2;
    }

    /**
     * Retourne la classe de l'argument demandé
     * @param num Numéro de l'argument
     * @return Point.class pour les deux arguments
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
            return "Choisir le deuxième point du triangle";
        } else if (num == 1) {
            return "Choisir le troisième point du triangle";
        }
        throw new ArgumentOperationException("Argument invalide: " + num);
    }

    /**
     * Retourne le titre de l'opération
     * @return "Calculer le cercle circonscrit"
     */
    @Override
    public String getTitle() {
        return "Calculer le cercle circonscrit";
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
        if (!(o instanceof Point)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Point");
        }
        
        if (num == 0) {
            pointB = (Point) o;
        } else if (num == 1) {
            pointC = (Point) o;
        } else {
            throw new ArgumentOperationException("Argument invalide: " + num);
        }
    }
} 