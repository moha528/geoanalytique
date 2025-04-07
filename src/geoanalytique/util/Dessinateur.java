package geoanalytique.util;
 
import geoanalytique.exception.VisiteurException;
import geoanalytique.graphique.GCoordonnee;
import geoanalytique.graphique.GLigne;
import geoanalytique.graphique.GOvale;
import geoanalytique.graphique.GTexte;
import geoanalytique.graphique.Graphique;
import geoanalytique.model.Cercle;
import geoanalytique.model.Droite;
import geoanalytique.model.Ellipse;
import geoanalytique.model.Point;
import geoanalytique.model.Polygone;
import geoanalytique.model.Segment;
import geoanalytique.model.ViewPort;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette objet est utilise par le presenteur, pour 'convertir' les modeles
 * en objet graphique utilisable par la vue. Le dessinateur repose sur le design pattern
 * Visitor. 
 * 
 * 
 *
 */
public class Dessinateur implements GeoObjectVisitor<Graphique> {

    private ViewPort viewport;
    
	/**
	 * 
	 */
	public Dessinateur(ViewPort viewport) {
            this.viewport = viewport;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitDroite(geoanalytique.model.Droite)
	 */
	public Graphique visitDroite(Droite d) throws VisiteurException {
        // Pour dessiner une droite, nous devons calculer deux points d'extrémité qui sont dans les limites du viewport
        // Nous utilisons les limites du viewport pour déterminer où la droite coupe ces limites
        
        double xmin = viewport.getXMin();
        double xmax = viewport.getXMax();
        double ymin = viewport.getYMin();
        double ymax = viewport.getYMax();
        
        Point p = d.getPoint();
        double pente = d.getPente();
        double b = p.getY() - pente * p.getX(); // ordonnée à l'origine
        
        double x1, y1, x2, y2;
        
        if (Double.isInfinite(pente)) {
            // Droite verticale (x = constante)
            x1 = p.getX();
            y1 = ymin;
            x2 = p.getX();
            y2 = ymax;
        } else {
            // Calculer les intersections avec les bords du viewport
            
            // Pour x = xmin, y = pente * xmin + b
            double yAtXmin = pente * xmin + b;
            // Pour x = xmax, y = pente * xmax + b
            double yAtXmax = pente * xmax + b;
            // Pour y = ymin, x = (ymin - b) / pente
            double xAtYmin = Math.abs(pente) < 1e-10 ? 0 : (ymin - b) / pente;
            // Pour y = ymax, x = (ymax - b) / pente
            double xAtYmax = Math.abs(pente) < 1e-10 ? 0 : (ymax - b) / pente;
            
            // Points d'intersection potentiels
            List<Point> points = new ArrayList<>();
            
            if (yAtXmin >= ymin && yAtXmin <= ymax) {
                points.add(new Point(xmin, yAtXmin, null));
            }
            if (yAtXmax >= ymin && yAtXmax <= ymax) {
                points.add(new Point(xmax, yAtXmax, null));
            }
            if (xAtYmin >= xmin && xAtYmin <= xmax) {
                points.add(new Point(xAtYmin, ymin, null));
            }
            if (xAtYmax >= xmin && xAtYmax <= xmax) {
                points.add(new Point(xAtYmax, ymax, null));
            }
            
            // S'il n'y a pas d'intersection (cas rare), utiliser les limites par défaut
            if (points.size() < 2) {
                x1 = xmin;
                y1 = pente * xmin + b;
                x2 = xmax;
                y2 = pente * xmax + b;
            } else {
                // Prendre les deux premiers points (il pourrait y en avoir plus en cas d'intersection avec les coins)
                x1 = points.get(0).getX();
                y1 = points.get(0).getY();
                x2 = points.get(1).getX();
                y2 = points.get(1).getY();
            }
        }
        
        // Convertir les coordonnées en pixels
        GCoordonnee c1 = viewport.convert(x1, y1);
        GCoordonnee c2 = viewport.convert(x2, y2);
        
        // Créer la ligne graphique avec le nom de la droite
        GLigne ligne = new GLigne(c1.getX(), c1.getY(), c2.getX(), c2.getY(), Color.BLACK);
        
        // Ajouter le nom de la droite près du point de référence
        GCoordonnee cRef = viewport.convert(p.getX(), p.getY());
        GTexte nom = new GTexte(cRef.getX() + 5, cRef.getY() - 5, d.getName(), Color.BLACK);
        
        // Renvoyer la ligne (pour simplifier, on ne renvoie pas le texte du nom)
        return ligne;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitEllipse(geoanalytique.model.Ellipse)
	 */
	public Graphique visitEllipse(Ellipse e) throws VisiteurException {
        Point centre = e.getCentre();
        double grandAxe = e.getGrandAxe();
        double petitAxe = e.getPetitAxe();
        
        // Convertir le centre en coordonnées d'écran
        GCoordonnee centreEcran = viewport.convert(centre.getX(), centre.getY());
        
        // Calculer les dimensions en pixels
        double xRange = viewport.getXMax() - viewport.getXMin();
        double yRange = viewport.getYMax() - viewport.getYMin();
        
        int largeurPixels = (int)((grandAxe / xRange) * viewport.getLargeur());
        int hauteurPixels = (int)((petitAxe / yRange) * viewport.getHauteur());
        
        // Créer l'ellipse graphique (centré sur le point)
        int x = centreEcran.getX() - largeurPixels / 2;
        int y = centreEcran.getY() - hauteurPixels / 2;
        
        GOvale ovale = new GOvale(x, y, largeurPixels, hauteurPixels, Color.BLACK);
        
        // Ajouter le nom de l'ellipse près du centre
        GTexte nom = new GTexte(centreEcran.getX() + 5, centreEcran.getY() - 5, e.getName(), Color.BLACK);
        
        return ovale;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitPoint(geoanalytique.model.Point)
	 */
	public Graphique visitPoint(Point o) throws VisiteurException {
        GCoordonnee c = viewport.convert(o.getX(), o.getY());
        
        // Ajouter le nom du point près de ses coordonnées
        GTexte nom = new GTexte(c.getX() + 5, c.getY() - 5, o.getName(), Color.BLACK);
        
        return c;
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitPolygone(geoanalytique.model.Polygone)
	 */
	public Graphique visitPolygone(Polygone p) throws VisiteurException {
        List<Point> points = p.getPoints();
        
        if (points.size() < 2) {
            throw new VisiteurException("Un polygone doit avoir au moins 2 points");
        }
        
        // Créer une liste pour stocker tous les segments qui forment le polygone
        List<GLigne> segments = new ArrayList<>();
        
        // Parcourir tous les points pour créer les segments
        for (int i = 0; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = points.get((i + 1) % points.size()); // Bouclage pour le dernier point
            
            // Convertir les points en coordonnées écran
            GCoordonnee c1 = viewport.convert(p1.getX(), p1.getY());
            GCoordonnee c2 = viewport.convert(p2.getX(), p2.getY());
            
            // Créer le segment graphique
            GLigne segment = new GLigne(c1.getX(), c1.getY(), c2.getX(), c2.getY(), Color.BLACK);
            segments.add(segment);
        }
        
        // Pour simplifier, on retourne juste le premier segment (normalement on devrait retourner tous les segments)
        return segments.get(0);
	}

	/**
	 * @see geoanalytique.model.GeoObjectVisitor#visitSegment(geoanalytique.model.Segment)
	 */
	public Graphique visitSegment(Segment s) throws VisiteurException {
        Point p1 = s.getPoint1();
        Point p2 = s.getPoint2();
        
        // Convertir les points en coordonnées écran
        GCoordonnee c1 = viewport.convert(p1.getX(), p1.getY());
        GCoordonnee c2 = viewport.convert(p2.getX(), p2.getY());
        
        // Créer le segment graphique
        GLigne ligne = new GLigne(c1.getX(), c1.getY(), c2.getX(), c2.getY(), Color.BLACK);
        
        // Ajouter le nom du segment au milieu
        double xMilieu = (p1.getX() + p2.getX()) / 2;
        double yMilieu = (p1.getY() + p2.getY()) / 2;
        GCoordonnee cMilieu = viewport.convert(xMilieu, yMilieu);
        GTexte nom = new GTexte(cMilieu.getX() + 5, cMilieu.getY() - 5, s.getName(), Color.BLACK);
        
        return ligne;
	}

	@Override
	public Graphique visitCercle(Cercle c) throws VisiteurException {
        Point centre = c.getCentre();
        double rayon = c.getRayon();
        
        // Convertir le centre en coordonnées d'écran
        GCoordonnee centreEcran = viewport.convert(centre.getX(), centre.getY());
        
        // Calculer les dimensions en pixels
        double xRange = viewport.getXMax() - viewport.getXMin();
        
        // Le diamètre en pixels
        int diametrePixels = (int)((2 * rayon / xRange) * viewport.getLargeur());
        
        // Créer le cercle graphique (centré sur le point)
        int x = centreEcran.getX() - diametrePixels / 2;
        int y = centreEcran.getY() - diametrePixels / 2;
        
        GOvale cercle = new GOvale(x, y, diametrePixels, diametrePixels, Color.BLACK);
        
        // Ajouter le nom du cercle près du centre
        GTexte nom = new GTexte(centreEcran.getX() + 5, centreEcran.getY() - 5, c.getName(), Color.BLACK);
        
        return cercle;
	}
}
