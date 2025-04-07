package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Point;
import geoanalytique.util.Operation;

/**
 * Classe concrète d'une opération permettant de déplacer un point
 * selon un vecteur (dx, dy)
 */
public class DeplacerPointOperation implements Operation {

    private Point point;
    private Double dx;
    private Double dy;
    
    /**
     * Constructeur de l'opération
     * @param point Point à déplacer
     */
    public DeplacerPointOperation(Point point) {
        this.point = point;
    }

    /**
     * Effectue le déplacement du point
     * @return null car l'opération ne crée pas de nouvel objet
     */
    @Override
    public Object calculer() {
        point.deplacer(dx, dy);
        return null; // Pas de résultat à renvoyer
    }

    /**
     * Retourne le nombre d'arguments nécessaires (2: dx et dy)
     * @return 2
     */
    @Override
    public int getArite() {
        return 2;
    }

    /**
     * Retourne la classe de l'argument demandé
     * @param num Numéro de l'argument (0 pour dx, 1 pour dy)
     * @return Double.class pour les deux arguments
     */
    @Override
    public Class getClassArgument(int num) {
        return Double.class;
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
            return "Déplacement en X";
        } else if (num == 1) {
            return "Déplacement en Y";
        }
        throw new ArgumentOperationException("Argument invalide: " + num);
    }

    /**
     * Retourne le titre de l'opération
     * @return "Déplacer le point"
     */
    @Override
    public String getTitle() {
        return "Déplacer le point";
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
        if (!(o instanceof Double)) {
            throw new IncorrectTypeOperationException("L'argument doit être un Double");
        }
        
        if (num == 0) {
            dx = (Double) o;
        } else if (num == 1) {
            dy = (Double) o;
        } else {
            throw new ArgumentOperationException("Argument invalide: " + num);
        }
    }
} 