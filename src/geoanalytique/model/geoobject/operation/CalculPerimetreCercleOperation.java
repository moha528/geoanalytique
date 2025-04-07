package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Cercle;
import geoanalytique.util.Operation;

/**
 * Classe concrète d'une opération permettant de calculer le périmètre d'un cercle
 */
public class CalculPerimetreCercleOperation implements Operation {

    private Cercle cercle;
    
    /**
     * Constructeur de l'opération
     * @param cercle Cercle dont on veut calculer le périmètre
     */
    public CalculPerimetreCercleOperation(Cercle cercle) {
        this.cercle = cercle;
    }

    /**
     * Calcule le périmètre du cercle
     * @return Double représentant le périmètre du cercle
     */
    @Override
    public Object calculer() {
        return 2 * Math.PI * cercle.getRayon();
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
     * @return "Calculer le périmètre"
     */
    @Override
    public String getTitle() {
        return "Calculer le périmètre";
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