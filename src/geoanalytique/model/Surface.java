package geoanalytique.model;

import geoanalytique.controleur.GeoAnalytiqueControleur;

/**
 * Classe de base pour les objets geometriques, ou il est possible de calculer
 * des operations specifiques aux surfaces (aire, point de gravite,...)
 * 
 */
public abstract class Surface extends GeoObject {
  
    
    // Ce constructeur EST INTERDIT d'utilisation
    // PAR CONSEQUENT IL NE FAUT PAS LE MODIFIER
    // OU MIEUX IL FAUT LE SUPPRIMER.
    // On laisse ce constructeur au debut, pour pouvoir compiler le programme
    // simplement
    public Surface() {
       throw new RuntimeException("INTERDICTION D'UTILISER CE CONSTRUCTEUR!!!!") ;
    }
    
    public Surface (GeoAnalytiqueControleur controleur) {
    	super(controleur);
    }

    public Surface(String name,GeoAnalytiqueControleur controleur) {
        super(name,controleur);
    }
    
    public abstract double calculerAire () ;

    public abstract Point calculerCentreGravite ();

}

