package geoanalytique.model.geoobject.operation;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.model.Polygone;
import geoanalytique.util.Operation;

/**
 * Classe concrète d'une opération permettant de calculer l'aire d'un polygone
 */
public class CalculAirePolygoneOperation implements Operation {

    private Polygone polygone;
    
    /**
     * Constructeur de l'opération
     * @param polygone Polygone dont on veut calculer l'aire
     */
    public CalculAirePolygoneOperation(Polygone polygone) {
        this.polygone = polygone;
    }

    /**
     * Calcule l'aire du polygone
     * @return Double représentant l'aire du polygone
     */
    @Override
    public Object calculer() {
        return polygone.calculerAire();
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
     * @return "Calculer l'aire"
     */
    @Override
    public String getTitle() {
        return "Calculer l'aire";
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