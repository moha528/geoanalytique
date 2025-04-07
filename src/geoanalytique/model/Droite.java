package geoanalytique.model;
 
import geoanalytique.util.GeoObjectVisitor;
import geoanalytique.controleur.GeoAnalytiqueControleur;
import geoanalytique.exception.VisiteurException;

/**
 * Modele mathematique pour les droites
 * Une droite est définie par un point et une pente
 */
public class Droite extends GeoObject {
    private Point point;
    private double pente;

    // Ce constructeur EST INTERDIT d'utilisation
    // PAR CONSEQUENT IL NE FAUT PAS LE MODIFIER
    // OU MIEUX IL FAUT LE SUPPRIMER.
    // On laisse ce constructeur au debut, pour pouvoir compiler le programme
    // simplement
    public Droite() {
       throw new RuntimeException("INTERDICTION D'UTILISER CE CONSTRUCTEUR!!!!") ;
    }
    
    /**
     * Constructeur d'une droite à partir d'un point et d'une pente
     * @param p Point appartenant à la droite
     * @param pente Pente de la droite
     * @param controleur Contrôleur principal de l'application
     */
    public Droite(Point p, double pente, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.point = p;
        this.pente = pente;
    }
    
    /**
     * Constructeur d'une droite à partir de deux points
     * @param p1 Premier point de la droite
     * @param p2 Deuxième point de la droite
     * @param controleur Contrôleur principal de l'application
     */
    public Droite(Point p1, Point p2, GeoAnalytiqueControleur controleur) {
        super(controleur);
        this.point = p1;
        this.pente = p1.calculPente(p2);
    }
    
    /**
     * Retourne le point de référence de la droite
     * @return Point de référence
     */
    public Point getPoint() {
        return point;
    }
    
    /**
     * Retourne la pente de la droite
     * @return Pente de la droite
     */
    public double getPente() {
        return pente;
    }
    
    /**
     * Calcule l'ordonnée à l'origine de la droite
     * @return Ordonnée à l'origine
     */
    public double calculerOrdonneeOrigine() {
        return point.getY() - pente * point.getX();
    }
    
    /**
     * Calcule la valeur de y pour une valeur de x donnée
     * @param x Valeur de x
     * @return Valeur de y correspondante sur la droite
     */
    public double calculerY(double x) {
        return pente * x + calculerOrdonneeOrigine();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Droite)) return false;
        
        Droite d = (Droite) o;
        
        // Deux droites sont égales si elles ont la même pente et la même ordonnée à l'origine
        return Math.abs(this.pente - d.pente) < Point.DELTA_PRECISION && 
               Math.abs(this.calculerOrdonneeOrigine() - d.calculerOrdonneeOrigine()) < Point.DELTA_PRECISION;
    }
    
    @Override
    public <T> T visitor(GeoObjectVisitor<T> obj) throws VisiteurException {
        return obj.visitDroite(this);
    }

    @Override
    public boolean contient(Point p) {
        if (Double.isInfinite(pente)) {
            // Si la pente est infinie (droite verticale), on vérifie si l'abscisse du point est égale à l'abscisse du point de référence
            return Math.abs(p.getX() - point.getX()) < Point.DELTA_PRECISION;
        } else {
            // Sinon, on vérifie si le point est sur la droite d'équation y = ax + b
            double y = calculerY(p.getX());
            return Math.abs(y - p.getY()) < Point.DELTA_PRECISION;
        }
    }
}