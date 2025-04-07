package geoanalytique.util;

import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;

/**
 * Interface de base a toutes les operations possibles pour un objet GeoAnalytique
 *  
 */
public interface Operation {
	
    /**
     * Permet d'associer un titre a l'operation courante
     * @return Renvoie le titre de l'operation
     */
    String getTitle();
	
   /**
    * Renvoie le nombre d'argument possible pour l'opération en cours
    * @return Renvoie le nombre d'argument possible pour l'opération en cours
    */
    int getArite();
    
    /**
     * Permet d'affecter les arguments en fonction de leur position.
     * @param num: numero d'argument devant etre modifie
     * @param o: valeur passer en argument
     * @throws ArgumentOperationException 
     * @throws IncorrectTypeOperationException 
     */
    void setArgument(int num, Object o) throws ArgumentOperationException, IncorrectTypeOperationException;
    
    /**
     * Cette fonction permet de connaitre la classe demandee pour l'argument <i>num</i>. On remarque
     * que les types primitifs doivent être représenté par les types envelloppe associe.
     * @param num: numero de l'argument défini dans le type
     * @return Renvoie la classe de l'argument <i>num</i>
     */
    Class getClassArgument(int num);
    
    /**
     * Cette fonction est lancee une fois lorsque tous les arguments ont ete defini.
     * Si l'opération génère un résultat quelconque elle est renvoyée en tant que résultat de la fonction.
     * Dans le cas contraire la fonction renvoie le pointeur <b>null</b>.
     * @return Renvoie l'objet résultant du calcul de l'opération
     */
    Object calculer();
    
    /**
     * Permet de donner à l'utilisateur la description complète pour l'argument <i>num</i>
     * De cette facon, le controleur principale sera a meme d'afficher cette information
     * pour l'utilisateur.
     * @param num: numero de l'argument concerne
     * @return Renvoie la description de l'argument specifie
     */
    String getDescriptionArgument(int num) throws ArgumentOperationException;
}
