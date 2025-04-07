package geoanalytique.controleur;
 
import geoanalytique.exception.ArgumentOperationException;
import geoanalytique.exception.IncorrectTypeOperationException;
import geoanalytique.exception.VisiteurException;
import geoanalytique.graphique.GCoordonnee;
import geoanalytique.graphique.GLigne;
import geoanalytique.graphique.Graphique;
import geoanalytique.gui.GeoAnalytiqueGUI;
import geoanalytique.model.GeoObject;
import geoanalytique.model.ViewPort;
import geoanalytique.util.Dessinateur;
import geoanalytique.util.Operation;

import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Cette classe est le controleur/presenteur principale. Tous les evenements importants
 * emanant de l'interface graphique doivent passer par cette classe.
 * 
 * 
 *
 */
public class GeoAnalytiqueControleur implements ActionListener, MouseListener, HierarchyBoundsListener {

	private ArrayList<GeoObject> objs;
	private ViewPort viewport;
	private GeoAnalytiqueGUI view;
	
	private transient GeoObject select;
	
        
		
	/**
	 * Constructeur de base afin de créer le lien entre la vue passee en
         * argument et le gestionnaire d'evenement.
         * @param view reference l'interface graphique dont le controleur sera le
         * gestionnaire d'evenement.
	 */
	public GeoAnalytiqueControleur(GeoAnalytiqueGUI view) {
		objs = new ArrayList<GeoObject>();
		this.view = view;
		viewport = new ViewPort(view.getCanvas().getWidth(),view.getCanvas().getWidth());
		// TODO: A completer avec vos modifications
	}

        /**
         * Cette fonction prend en charge l'ajout dans le système d'un nouvel
         * objet geometrique. On pourra en particulier, mettre a jour la vue
         * et la liste des operations realisable sur l'application. 
         * D'autre taches pourront etre realisees si besoin est.
         * @param obj objet geometrique a ajouter dans le systeme
         */
	public void addObjet(GeoObject obj) {
            objs.add(obj);
            
            // Mise à jour de la liste des objets dans l'interface
            updateObjectsList();
            
            // Si aucun objet n'est sélectionné, on sélectionne ce nouvel objet
            if (select == null) {
                selectionner(obj);
            }
            
            // Redessiner les points sur la vue
            recalculPoints();
	}
	
	
	/**
         * Cette fonction est appele par le modele pour prevenir le controleur
         * d'une mise a jour de l'objet geometrique passe en argument. Le 
         * controleur peut donc mettre a jour les informations de la vue, si 
         * c'est necessaire.
         * @param object objet ayant subit une modification
         */
	public void update(GeoObject object) {
            // Mise à jour de la liste des objets
            updateObjectsList();
            
            // Si l'objet modifié est l'objet sélectionné, on met à jour la liste des opérations
            if (object == select) {
                updateOperationsList();
            }
            
            // Redessiner la vue
            recalculPoints();
	}

	public void actionPerformed(ActionEvent e) {
		// TODO: a completer
	}

	public void mouseClicked(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}
	

	public void mouseReleased(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void mouseEntered(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void mouseExited(MouseEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void mousePressed(MouseEvent e) {
            // TODO: a completer pour un clique souris dans le canevas
	}

        /**
         * Cette fonction permet de realiser toutes les taches inherante a la
         * selection d'un objet geometrique dans la vue. Cette fonction est tres
         * utile pour marquer l'objet selectionne de maniere plus significative.
         * 
         * @param o objet a selectionne
         */
	private void selectionner(GeoObject o) {
            // Déselectionner l'objet actuel s'il y en a un
            if (select != null) {
                deselectionner();
            }
            
            // Définir le nouvel objet sélectionné
            select = o;
            
            // Mettre en évidence l'objet dans la liste des objets
            int index = objs.indexOf(o);
            if (index != -1) {
                view.getListObjects().setSelectedIndex(index);
            }
            
            // Mettre à jour la liste des opérations disponibles pour cet objet
            updateOperationsList();
            
            // Mettre à jour la vue pour afficher l'objet sélectionné en surbrillance
            try {
                Dessinateur d = new Dessinateur(viewport);
                Graphique g = o.visitor(d);
                g.setCouleur(Color.RED); // Couleur de sélection
                view.getCanvas().selectGraphique(g);
                view.getCanvas().repaint();
            } catch (VisiteurException e) {
                e.printStackTrace();
            }
	}
	
	/**
         * Operation permettant de deselectionner le dernier objet selectionne
         * (si il existe). On pourra enlever tous marqueurs present sur l'interface
         * graphique a ce moment ainsi que les operations anciennement realisable.
         */	
	private void deselectionner() {
            if (select != null) {
                // Effacer la sélection actuelle
                select = null;
                
                // Désélectionner dans la liste d'objets
                view.getListObjects().clearSelection();
                
                // Vider la liste des opérations
                view.getListOperations().setListData(new String[0]);
                
                // Redessiner la vue sans surbrillance
                recalculPoints();
            }
	}
	
    /**
     * Met à jour la liste des objets dans l'interface
     */
    private void updateObjectsList() {
        // Créer un tableau de noms d'objets
        String[] objectNames = new String[objs.size()];
        for (int i = 0; i < objs.size(); i++) {
            objectNames[i] = objs.get(i).getName();
        }
        
        // Mettre à jour la JList dans l'interface
        view.getListObjects().setListData(objectNames);
    }
    
    /**
     * Met à jour la liste des opérations pour l'objet sélectionné
     */
    private void updateOperationsList() {
        if (select != null) {
            // Récupérer les opérations disponibles pour l'objet sélectionné
            ArrayList<Operation> operations = select.getOperations();
            String[] operationNames = new String[operations.size()];
            
            // Créer un tableau de noms d'opérations
            for (int i = 0; i < operations.size(); i++) {
                operationNames[i] = operations.get(i).getTitle();
            }
            
            // Mettre à jour la JList dans l'interface
            view.getListOperations().setListData(operationNames);
        }
    }

        /**
         * Cette fonction est appele uniquement lorsque la liaison controleur et
         * interface graphique a ete realisee. Elle permet de realiser certaines
         * taches necessaires a ce moment. Comme par exemple ajouter un listener
         * aux boutons etc.
         */
	public void prepareTout() {
            // Preparation des evenements du canevas
            view.getCanvas().addMouseListener(this);
            view.getCanvas().addHierarchyBoundsListener(this);
            
            // TODO: a completer si necessaire
            
            
	}

	public void ancestorMoved(HierarchyEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void ancestorResized(HierarchyEvent e) {
	    // TODO: a completer si le canevas est redimentionnable
	}

        /**
         * Cette fonction est la fonction permettant de caracteriser le presenteur.
         * Elle realise la presentation des donnees en indiquant a la vue les
         * element graphique devant etre affiche en fonction de la zone d'affichage
         * (Viewport).
         */
	private void recalculPoints() {
		
            // on nettoie les anciennes images
            view.getCanvas().clear();
            
            // Ajout des axes Ox et Oy
            // Axe des abscisses (Ox)
            double xmin = viewport.getXMin();
            double xmax = viewport.getXMax();
            double ymin = viewport.getYMin();
            double ymax = viewport.getYMax();
            
            // Convertir les coordonnées pour l'axe Ox (y = 0)
            GCoordonnee oxStart = viewport.convert(xmin, 0);
            GCoordonnee oxEnd = viewport.convert(xmax, 0);
            // Créer la ligne pour l'axe Ox
            GLigne axeX = new GLigne(oxStart.getX(), oxStart.getY(), oxEnd.getX(), oxEnd.getY(), Color.BLACK);
            view.getCanvas().addGraphique(axeX);
            
            // Convertir les coordonnées pour l'axe Oy (x = 0)
            GCoordonnee oyStart = viewport.convert(0, ymin);
            GCoordonnee oyEnd = viewport.convert(0, ymax);
            // Créer la ligne pour l'axe Oy
            GLigne axeY = new GLigne(oyStart.getX(), oyStart.getY(), oyEnd.getX(), oyEnd.getY(), Color.BLACK);
            view.getCanvas().addGraphique(axeY);
            
            // redessine toutes les figures
            Dessinateur d = new Dessinateur(viewport);
            for (GeoObject o : objs) {
            	Graphique c;
		try {
                    c = o.visitor(d);
                    view.getCanvas().addGraphique(c);
                } catch (VisiteurException e) {
                    e.printStackTrace();
                }
            }
            
            view.getCanvas().repaint();
	}

	
        /**
         * Cette fonction permet de lancer une operation sur un objet. A priori
         * Elle n'a pas a etre modifiee dans un premier temps. Sauf si vous voulez
         * modifier le comportement de celle-ci en donnant un aspect plus jolie.
         * @param object objet sur lequel realise l'operation
         * @param ope operation devant etre realise sur l'objet <i>object</i>
         */
	public void lanceOperation(GeoObject object, Operation ope) {
            // TODO: a modifier si vous avez compris comment la fonction
            // procedais. Sinon laissez telle quel
		for(int i=0; i < ope.getArite();i++) {
			try {
				String res = JOptionPane.showInputDialog(view, ope.getDescriptionArgument(i), ope.getTitle(),JOptionPane.QUESTION_MESSAGE);
				if(res == null)
					return;
				if(ope.getClassArgument(i) == Double.class) {
					ope.setArgument(i, new Double(res));
				}
				else if(ope.getClassArgument(i) == Integer.class) {
					ope.setArgument(i, new Integer(res));
				}
				else if(ope.getClassArgument(i) == Character.class) {
					ope.setArgument(i, new Character(res.charAt(0)));
				}
				else if(ope.getClassArgument(i) == String.class) {
					ope.setArgument(i, new String(res));
				}
				else if(GeoObject.class.isAssignableFrom(ope.getClassArgument(i))) {
					ope.setArgument(i, searchObject(res));
				}
				else {
                                    JOptionPane.showMessageDialog(view, "Classe de l'argument non supporte", "Erreur dans le lancement de l'operation", JOptionPane.ERROR_MESSAGE);
       				    return;
				}
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (ArgumentOperationException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IncorrectTypeOperationException e) {
				e.printStackTrace();
			}
		}
                // Dans notre application on autorise un resultat, que nous devons
                // interprété. Pas de resultat correspond au pointeur null
		Object o = ope.calculer();
		if(o != null) {
                       // on a bien trouve un resultat. Mais on ne connait pas
                       // sa nature on va donc le tester
			if(GeoObject.class.isAssignableFrom(o.getClass())) {
                            // c'est un objet analytique on l'ajoute dans notre systeme
				addObjet((GeoObject) o);
			}
			else {
                            // on ne connait pas le type, donc on l'avise a l'utilisateur
				JOptionPane.showConfirmDialog(view, o, ope.getTitle(),JOptionPane.OK_OPTION);
			}
                        // TODO BONUS: proposer et modifier le traitement du resultat
                        // pour pouvoir renvoyer plusieurs chose en meme temps
		}
		recalculPoints();
	}
	
        /**
         * Cette fonction permet de retrouver un objet dans la liste des objets
         * geometrique a partir de son nom (que l'on supposera unique). Si le nom
         * de l'objet est un introuvable on leve l'exception ArgumentOperationException.
         * Cette fonction est utilisee dans le calcul d'une operation.
         * @param x nom de l'objet a rechercher
         * @return Renvoie l'objet ayant pour nom x, sinon leve une exception
         * @throws geoanalytique.exception.ArgumentOperationException
         */
	private Object searchObject(String x) throws ArgumentOperationException {
		for (GeoObject o : objs) {
			if(o.getName().equals(x))
				return o;
		}
		throw new ArgumentOperationException("Nom de l'objet introuvable");
	}
}
