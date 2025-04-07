package geoanalytique.model;
 
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;
import geoanalytique.model.geoobject.operation.ChangeNomOperation;
import geoanalytique.util.Operation;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Classe de base a tous objets geometriques
 * 
 */
public abstract class GeoObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int count = 0;
    private ArrayList<Operation> operations;
    
    private String name;
    private transient GeoAnalytiqueControleur controleur;

    // Ce constructeur EST INTERDIT d'utilisation
    // PAR CONSEQUENT IL NE FAUT PAS LE MODIFIER
    // OU MIEUX IL FAUT LE SUPPRIMER.
    // On laisse ce constructeur au debut, pour pouvoir compiler le programme
    // simplement
    public GeoObject() {
       throw new RuntimeException("INTERDICTION D'UTILISER CE CONSTRUCTEUR!!!!") ;
    }
    
    /**
     * Constructeur d'un objet géométrique avec un nom spécifique
     * @param name Nom de l'objet
     * @param controleur Contrôleur principal de l'application
     */
    public GeoObject(String name, GeoAnalytiqueControleur controleur) {
        operations = new ArrayList<Operation>();
        operations.add(new ChangeNomOperation(this));
        this.name = name;
        this.controleur = controleur;
    }
    
    /**
     * Constructeur d'un objet géométrique avec un nom généré automatiquement
     * @param controleur Contrôleur principal de l'application
     */
    public GeoObject(GeoAnalytiqueControleur controleur) {
        this.name = this.getClass().getSimpleName()+(count++);
        this.controleur = controleur;
        operations = new ArrayList<Operation>();
        operations.add(new ChangeNomOperation(this));
    }

    /**
     * Retourne le nom de l'objet géométrique
     * @return Nom de l'objet
     */
    public String getName() {
        return name;
    }
    
    /**
     * Retourne la liste des opérations disponibles pour cet objet
     * @return Liste des opérations
     */
    public ArrayList<Operation> getOperations() {
        return operations;
    }
    
    /**
     * Notifie le contrôleur que l'objet a été modifié
     */
    public void modifie() {
        if (controleur != null) {
            controleur.update(this);
        }
    }

    /**
     * Modifie le nom de l'objet géométrique
     * @param name Nouveau nom
     */
    public void setName(String name) {
        this.name = name;
        modifie();
    }

    /**
     * Vérifie si un point est contenu dans cet objet géométrique
     * @param p Point à tester
     * @return true si le point est contenu dans l'objet, false sinon
     */
    public abstract boolean contient(Point p);
    
    /**
     * Accepte un visiteur pour l'objet géométrique
     * @param obj Visiteur à accepter
     * @return Résultat du visiteur
     * @throws VisiteurException Si une erreur survient pendant la visite
     */
    public abstract <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException;

    /**
     * Définit le contrôleur associé à cet objet
     * Utilisé principalement lors de la désérialisation
     * @param controleur Nouveau contrôleur
     */
    public void setControleur(GeoAnalytiqueControleur controleur) {
        this.controleur = controleur;
    }
    
    /**
     * Retourne le contrôleur associé à cet objet
     * @return Contrôleur associé
     */
    public GeoAnalytiqueControleur getControleur() {
        return controleur;
    }
}

