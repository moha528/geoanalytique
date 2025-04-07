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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	private boolean isSelectionProgrammatic = false;
	
        
		
	/**
	 * Constructeur de base afin de créer le lien entre la vue passee en
         * argument et le gestionnaire d'evenement.
         * @param view reference l'interface graphique dont le controleur sera le
         * gestionnaire d'evenement.
	 */
	public GeoAnalytiqueControleur(GeoAnalytiqueGUI view) {
		objs = new ArrayList<GeoObject>();
		this.view = view;
		
		// Initialiser le viewport avec les dimensions actuelles du canevas
		int canvasWidth = view.getCanvas().getWidth();
		int canvasHeight = view.getCanvas().getHeight();
		
		// Utiliser des dimensions par défaut si le canevas n'est pas encore dimensionné
		if (canvasWidth <= 0 || canvasHeight <= 0) {
			canvasWidth = 600; // Valeur par défaut cohérente avec la taille préférée du canevas
			canvasHeight = 500;
		}
		
		viewport = new ViewPort(canvasWidth, canvasHeight);
		
		// Ajouter les listeners pour les boutons de création d'objets
		view.getBtnCreatePoint().addActionListener(this);
		view.getBtnCreateSegment().addActionListener(this);
		view.getBtnCreateDroite().addActionListener(this);
		view.getBtnCreateCercle().addActionListener(this);
		view.getBtnCreatePolygone().addActionListener(this);
		view.getBtnExecuteOperation().addActionListener(this);
		
		// Ajouter les listeners pour les fonctionnalités avancées
		view.getBtnZoomIn().addActionListener(this);
		view.getBtnZoomOut().addActionListener(this);
		view.getBtnZoomReset().addActionListener(this);
		view.getBtnSaveImage().addActionListener(this);
		view.getBtnSaveProject().addActionListener(this);
		view.getBtnOpenProject().addActionListener(this);
		
		// Ajouter les listeners pour les listes d'objets et d'opérations
		view.getListObjects().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && !isSelectionProgrammatic) {
				int selectedIndex = view.getListObjects().getSelectedIndex();
				if (selectedIndex >= 0 && selectedIndex < objs.size()) {
					selectionner(objs.get(selectedIndex));
				}
			}
		});
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
		Object source = e.getSource();
		
		// Traiter les actions pour les fonctionnalités avancées
		if (source == view.getBtnZoomIn()) {
            view.getCanvas().zoomIn();
        } else if (source == view.getBtnZoomOut()) {
            view.getCanvas().zoomOut();
        } else if (source == view.getBtnZoomReset()) {
            view.getCanvas().resetZoom();
        } else if (source == view.getBtnSaveImage()) {
            saveCanvasAsImage();
        } else if (source == view.getBtnSaveProject()) {
            saveProject();
        } else if (source == view.getBtnOpenProject()) {
            openProject();
        }
		// Traiter les actions des boutons de création d'objets
		else if (source == view.getBtnCreatePoint()) {
			// Demander les coordonnées du point
			try {
				String name = JOptionPane.showInputDialog(view, "Nom du point:", "Point" + objs.size());
				if (name == null) return; // Annulation
				
				String xStr = JOptionPane.showInputDialog(view, "Coordonnée X:", "0");
				if (xStr == null) return; // Annulation
				double x = Double.parseDouble(xStr);
				
				String yStr = JOptionPane.showInputDialog(view, "Coordonnée Y:", "0");
				if (yStr == null) return; // Annulation
				double y = Double.parseDouble(yStr);
				
				// Créer le point et l'ajouter
				addObjet(new geoanalytique.model.Point(name, x, y, this));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(view, "Valeurs numériques invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == view.getBtnCreateSegment()) {
			// Créer un segment à partir de deux points existants
			try {
				// Récupérer la liste des points disponibles
				ArrayList<geoanalytique.model.Point> points = new ArrayList<>();
				for (GeoObject obj : objs) {
					if (obj instanceof geoanalytique.model.Point) {
						points.add((geoanalytique.model.Point) obj);
					}
				}
				
				if (points.size() < 2) {
					JOptionPane.showMessageDialog(view, "Vous devez avoir au moins 2 points pour créer un segment", 
												"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String[] pointNames = new String[points.size()];
				for (int i = 0; i < points.size(); i++) {
					pointNames[i] = points.get(i).getName();
				}
				
				String name = JOptionPane.showInputDialog(view, "Nom du segment:", "Segment" + objs.size());
				if (name == null) return; // Annulation
				
				String p1Str = (String) JOptionPane.showInputDialog(view, "Premier point:", "Sélectionner un point", 
																JOptionPane.QUESTION_MESSAGE, null, pointNames, pointNames[0]);
				if (p1Str == null) return; // Annulation
				
				String p2Str = (String) JOptionPane.showInputDialog(view, "Second point:", "Sélectionner un point", 
																JOptionPane.QUESTION_MESSAGE, null, pointNames, pointNames[0]);
				if (p2Str == null) return; // Annulation
				
				// Trouver les points correspondants
				geoanalytique.model.Point p1 = null, p2 = null;
				for (geoanalytique.model.Point p : points) {
					if (p.getName().equals(p1Str)) p1 = p;
					if (p.getName().equals(p2Str)) p2 = p;
				}
				
				// Créer et ajouter le segment
				if (p1 != null && p2 != null) {
					addObjet(new geoanalytique.model.Segment(p1, p2, this));
				}
				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Erreur lors de la création du segment: " + ex.getMessage(), 
										   "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == view.getBtnCreateDroite()) {
			// Similaire au segment, créer une droite à partir d'un point et d'une pente
			try {
				// Récupérer la liste des points existants
				ArrayList<geoanalytique.model.Point> points = new ArrayList<>();
				for (GeoObject obj : objs) {
					if (obj instanceof geoanalytique.model.Point) {
						points.add((geoanalytique.model.Point) obj);
					}
				}
				
				if (points.isEmpty()) {
					JOptionPane.showMessageDialog(view, "Vous devez avoir au moins 1 point pour créer une droite", 
												"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String[] pointNames = new String[points.size()];
				for (int i = 0; i < points.size(); i++) {
					pointNames[i] = points.get(i).getName();
				}
				
				String name = JOptionPane.showInputDialog(view, "Nom de la droite:", "Droite" + objs.size());
				if (name == null) return; // Annulation
				
				String pStr = (String) JOptionPane.showInputDialog(view, "Point de la droite:", "Sélectionner un point", 
																JOptionPane.QUESTION_MESSAGE, null, pointNames, pointNames[0]);
				if (pStr == null) return; // Annulation
				
				String penteStr = JOptionPane.showInputDialog(view, "Pente de la droite:", "1");
				if (penteStr == null) return; // Annulation
				double pente = Double.parseDouble(penteStr);
				
				// Trouver le point correspondant
				geoanalytique.model.Point p = null;
				for (geoanalytique.model.Point point : points) {
					if (point.getName().equals(pStr)) {
						p = point;
						break;
					}
				}
				
				// Créer et ajouter la droite
				if (p != null) {
					addObjet(new geoanalytique.model.Droite(name, p, pente, this));
				}
				
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(view, "Valeur de pente invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Erreur lors de la création de la droite: " + ex.getMessage(), 
										   "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == view.getBtnCreateCercle()) {
			// Créer un cercle à partir d'un centre et d'un rayon
			try {
				// Récupérer la liste des points existants pour le centre
				ArrayList<geoanalytique.model.Point> points = new ArrayList<>();
				for (GeoObject obj : objs) {
					if (obj instanceof geoanalytique.model.Point) {
						points.add((geoanalytique.model.Point) obj);
					}
				}
				
				if (points.isEmpty()) {
					JOptionPane.showMessageDialog(view, "Vous devez avoir au moins 1 point pour créer un cercle", 
												"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String[] pointNames = new String[points.size()];
				for (int i = 0; i < points.size(); i++) {
					pointNames[i] = points.get(i).getName();
				}
				
				String name = JOptionPane.showInputDialog(view, "Nom du cercle:", "Cercle" + objs.size());
				if (name == null) return; // Annulation
				
				String centreStr = (String) JOptionPane.showInputDialog(view, "Centre du cercle:", "Sélectionner un point", 
																	JOptionPane.QUESTION_MESSAGE, null, pointNames, pointNames[0]);
				if (centreStr == null) return; // Annulation
				
				String rayonStr = JOptionPane.showInputDialog(view, "Rayon du cercle:", "1");
				if (rayonStr == null) return; // Annulation
				double rayon = Double.parseDouble(rayonStr);
				
				// Trouver le point correspondant
				geoanalytique.model.Point centre = null;
				for (geoanalytique.model.Point p : points) {
					if (p.getName().equals(centreStr)) {
						centre = p;
						break;
					}
				}
				
				// Créer et ajouter le cercle
				if (centre != null) {
					addObjet(new geoanalytique.model.Cercle(name, centre, rayon, this));
				}
				
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(view, "Valeur de rayon invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Erreur lors de la création du cercle: " + ex.getMessage(), 
										   "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == view.getBtnCreatePolygone()) {
			// Créer un polygone à partir d'une liste de points
			try {
				// Récupérer la liste des points existants
				ArrayList<geoanalytique.model.Point> points = new ArrayList<>();
				for (GeoObject obj : objs) {
					if (obj instanceof geoanalytique.model.Point) {
						points.add((geoanalytique.model.Point) obj);
					}
				}
				
				if (points.size() < 3) {
					JOptionPane.showMessageDialog(view, "Vous devez avoir au moins 3 points pour créer un polygone", 
												"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String[] pointNames = new String[points.size()];
				for (int i = 0; i < points.size(); i++) {
					pointNames[i] = points.get(i).getName();
				}
				
				String name = JOptionPane.showInputDialog(view, "Nom du polygone:", "Polygone" + objs.size());
				if (name == null) return; // Annulation
				
				// Demander le nombre de sommets
				String nbPointsStr = JOptionPane.showInputDialog(view, "Nombre de sommets (3 minimum):", "3");
				if (nbPointsStr == null) return; // Annulation
				int nbPoints = Integer.parseInt(nbPointsStr);
				
				if (nbPoints < 3) {
					JOptionPane.showMessageDialog(view, "Un polygone doit avoir au moins 3 sommets", 
												"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (nbPoints > points.size()) {
					JOptionPane.showMessageDialog(view, "Il n'y a pas assez de points disponibles", 
												"Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				// Sélectionner les points pour le polygone
				ArrayList<geoanalytique.model.Point> sommets = new ArrayList<>();
				for (int i = 0; i < nbPoints; i++) {
					String pointStr = (String) JOptionPane.showInputDialog(view, "Point " + (i+1) + ":", 
															"Sélectionner un point", JOptionPane.QUESTION_MESSAGE, 
															null, pointNames, pointNames[0]);
					if (pointStr == null) return; // Annulation
					
					// Trouver le point correspondant
					for (geoanalytique.model.Point p : points) {
						if (p.getName().equals(pointStr)) {
							sommets.add(p);
							break;
						}
					}
				}
				
				// Créer et ajouter le polygone
				if (sommets.size() == nbPoints) {
					addObjet(new geoanalytique.model.Polygone(name, sommets, this));
				}
				
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(view, "Valeur numérique invalide", "Erreur", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(view, "Erreur lors de la création du polygone: " + ex.getMessage(), 
										   "Erreur", JOptionPane.ERROR_MESSAGE);
			}
		} else if (source == view.getBtnExecuteOperation()) {
			// Exécuter l'opération sélectionnée
			if (select != null) {
				int selectedIndex = view.getListOperations().getSelectedIndex();
				if (selectedIndex >= 0 && selectedIndex < select.getOperations().size()) {
					Operation operation = select.getOperations().get(selectedIndex);
					try {
						lanceOperation(select, operation);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(view, 
							"Erreur lors de l'exécution de l'opération: " + ex.getMessage(), 
							"Erreur", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
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
		// Convertir les coordonnées de la souris en coordonnées du monde réel
		int canvasX = e.getX();
		int canvasY = e.getY();
		
		// Déselectionner l'objet actuel
		deselectionner();
		
		// Rechercher un objet géométrique proche du point cliqué
		for (GeoObject obj : objs) {
			try {
				// Convertir les coordonnées de l'objet en coordonnées écran
				Dessinateur d = new Dessinateur(viewport);
				Graphique g = obj.visitor(d);
				
				// Vérifier si le clic est sur cet objet
				if (isNear(g, canvasX, canvasY, 10)) { // 10 pixels de tolérance
					selectionner(obj);
					break;
				}
			} catch (VisiteurException ex) {
				// Ignorer et continuer avec le prochain objet
			}
		}
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
                // Set the flag to true before programmatically setting the selection
                isSelectionProgrammatic = true;
                view.getListObjects().setSelectedIndex(index);
                // Reset the flag after setting the selection
                isSelectionProgrammatic = false;
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
            
            // Ajouter les listeners pour les boutons d'exécution d'opérations
            view.getBtnExecuteOperation().addActionListener(e -> {
                if (select != null) {
                    int selectedIndex = view.getListOperations().getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < select.getOperations().size()) {
                        Operation operation = select.getOperations().get(selectedIndex);
                        try {
                            lanceOperation(select, operation);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(view, 
                                "Erreur lors de l'exécution de l'opération: " + ex.getMessage(), 
                                "Erreur", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            });
            
            // Ajouter un listener pour le bouton Supprimer
            view.getBtnSupprimer().addActionListener(e -> {
                int selectedIndex = view.getListObjects().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < objs.size()) {
                    // Vérifier les dépendances avant de supprimer
                    GeoObject objToRemove = objs.get(selectedIndex);
                    
                    // Demander confirmation
                    int confirm = JOptionPane.showConfirmDialog(view,
                        "Êtes-vous sûr de vouloir supprimer l'objet " + objToRemove.getName() + " ?",
                        "Confirmation de suppression",
                        JOptionPane.YES_NO_OPTION);
                        
                    if (confirm == JOptionPane.YES_OPTION) {
                        // Si l'objet est sélectionné, le désélectionner d'abord
                        if (objToRemove == select) {
                            deselectionner();
                        }
                        
                        // Supprimer l'objet de la liste
                        objs.remove(selectedIndex);
                        
                        // Mettre à jour l'interface
                        updateObjectsList();
                        recalculPoints();
                    }
                }
            });
            
            // Ajouter un listener pour le bouton Renommer
            view.getBtnRenommer().addActionListener(e -> {
                int selectedIndex = view.getListObjects().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < objs.size()) {
                    GeoObject objToRename = objs.get(selectedIndex);
                    
                    // Demander le nouveau nom
                    String newName = JOptionPane.showInputDialog(view,
                        "Nouveau nom pour " + objToRename.getName() + " :",
                        objToRename.getName());
                        
                    if (newName != null && !newName.isEmpty()) {
                        // Vérifier que le nom n'est pas déjà utilisé
                        boolean nameExists = false;
                        for (GeoObject obj : objs) {
                            if (obj != objToRename && obj.getName().equals(newName)) {
                                nameExists = true;
                                break;
                            }
                        }
                        
                        if (nameExists) {
                            JOptionPane.showMessageDialog(view,
                                "Ce nom est déjà utilisé par un autre objet.",
                                "Erreur de renommage",
                                JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Renommer l'objet
                            try {
                                // Utiliser l'opération de changement de nom si elle existe
                                for (Operation op : objToRename.getOperations()) {
                                    if (op.getTitle().contains("Changer le nom")) {
                                        op.setArgument(0, newName);
                                        op.calculer();
                                        break;
                                    }
                                }
                                
                                // Mettre à jour l'interface
                                updateObjectsList();
                                
                                // Si l'objet était sélectionné, mettre à jour la sélection
                                if (objToRename == select) {
                                    updateOperationsList();
                                }
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(view,
                                    "Erreur lors du renommage: " + ex.getMessage(),
                                    "Erreur",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
            
            // Gérer le redimensionnement du canevas
            view.getCanvas().addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int newWidth = view.getCanvas().getWidth();
                    int newHeight = view.getCanvas().getHeight();
                    
                    if (newWidth > 0 && newHeight > 0) {
                        viewport.resize(newWidth, newHeight);
                        recalculPoints();
                    }
                }
            });
            
            // Initialize the viewport properly with current canvas size
            int initialWidth = view.getCanvas().getWidth();
            int initialHeight = view.getCanvas().getHeight();
            if (initialWidth > 0 && initialHeight > 0) {
                viewport.resize(initialWidth, initialHeight);
            }
            
            // Dessiner la vue initiale
            recalculPoints();
	}

	public void ancestorMoved(HierarchyEvent e) {
            // a priori inutile
            // mais customisable si necessaire
	}

	public void ancestorResized(HierarchyEvent e) {
	    // Mettre à jour le viewport lorsque le canevas est redimensionné
	    int newWidth = view.getCanvas().getWidth();
	    int newHeight = view.getCanvas().getHeight();
	    
	    if (newWidth > 0 && newHeight > 0) {
	        viewport.resize(newWidth, newHeight);
	        recalculPoints();
	    }
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

	/**
	 * Vérifie si des coordonnées sont proches d'un graphique
	 * @param g Graphique à tester
	 * @param x Coordonnée X à tester
	 * @param y Coordonnée Y à tester
	 * @param tolerance Distance maximale considérée comme "proche"
	 * @return true si les coordonnées sont proches du graphique
	 */
	private boolean isNear(Graphique g, int x, int y, int tolerance) {
		// Pour simplifier, on considère que chaque objet est sélectionnable
		// dans un rayon de 'tolerance' pixels autour de sa position
		
		// Traitement spécifique pour les différents types de graphiques
		if (g instanceof GCoordonnee) {
			GCoordonnee coord = (GCoordonnee) g;
			int dx = coord.getX() - x;
			int dy = coord.getY() - y;
			return (dx * dx + dy * dy) <= tolerance * tolerance; // Distance euclidienne
		} else if (g instanceof GLigne) {
			GLigne ligne = (GLigne) g;
			// Calcul de la distance d'un point à une ligne
			return isNearLine(x, y, ligne.getX1(), ligne.getY1(), ligne.getX2(), ligne.getY2(), tolerance);
		}
		
		// Par défaut, on considère qu'on est trop loin
		return false;
	}

	/**
	 * Vérifie si un point est proche d'une ligne
	 * @param x Coordonnée X du point
	 * @param y Coordonnée Y du point
	 * @param x1 Coordonnée X du premier point de la ligne
	 * @param y1 Coordonnée Y du premier point de la ligne
	 * @param x2 Coordonnée X du second point de la ligne
	 * @param y2 Coordonnée Y du second point de la ligne
	 * @param tolerance Distance maximale considérée comme "proche"
	 * @return true si le point est proche de la ligne
	 */
	private boolean isNearLine(int x, int y, int x1, int y1, int x2, int y2, int tolerance) {
		// Calcul de la distance d'un point à une ligne
		double lineLength = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		if (lineLength == 0) return false; // Ligne de longueur nulle
		
		double distanceToLine = Math.abs((y2 - y1) * x - (x2 - x1) * y + x2 * y1 - y2 * x1) / lineLength;
		
		// Vérifier si le point est dans le rectangle englobant la ligne
		boolean inBox = (x >= Math.min(x1, x2) - tolerance && 
						x <= Math.max(x1, x2) + tolerance && 
						y >= Math.min(y1, y2) - tolerance && 
						y <= Math.max(y1, y2) + tolerance);
		
		return distanceToLine <= tolerance && inBox;
	}

	/**
     * Sauvegarde le canevas sous forme d'image
     */
    private void saveCanvasAsImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer l'image");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG Image", "jpg"));
        
        int userSelection = fileChooser.showSaveDialog(view);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];
            
            // S'assurer que le fichier a la bonne extension
            if (!fileToSave.getName().toLowerCase().endsWith("." + extension)) {
                fileToSave = new File(fileToSave.getAbsolutePath() + "." + extension);
            }
            
            try {
                // Créer une image à partir du canevas
                BufferedImage image = new BufferedImage(
                    view.getCanvas().getWidth(),
                    view.getCanvas().getHeight(),
                    BufferedImage.TYPE_INT_RGB
                );
                
                // Rendre le canevas sur l'image
                view.getCanvas().paint(image.getGraphics());
                
                // Enregistrer l'image
                ImageIO.write(image, extension, fileToSave);
                
                JOptionPane.showMessageDialog(view,
                    "Image enregistrée avec succès: " + fileToSave.getName(),
                    "Enregistrement réussi",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view,
                    "Erreur lors de l'enregistrement de l'image: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Sauvegarde le projet courant (tous les objets géométriques)
     */
    private void saveProject() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Projet GéoAnalytique", "geo"));
        
        int userSelection = fileChooser.showSaveDialog(view);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // S'assurer que le fichier a la bonne extension
            if (!fileToSave.getName().toLowerCase().endsWith(".geo")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".geo");
            }
            
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileToSave))) {
                // Enregistrer le nombre d'objets
                oos.writeInt(objs.size());
                
                // Enregistrer chaque objet
                for (GeoObject obj : objs) {
                    oos.writeObject(obj);
                }
                
                JOptionPane.showMessageDialog(view,
                    "Projet enregistré avec succès: " + fileToSave.getName(),
                    "Enregistrement réussi",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view,
                    "Erreur lors de l'enregistrement du projet: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Ouvre un projet précédemment sauvegardé
     */
    private void openProject() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir un projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Projet GéoAnalytique", "geo"));
        
        int userSelection = fileChooser.showOpenDialog(view);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileToOpen))) {
                // Effacer les objets existants
                objs.clear();
                view.getCanvas().clear();
                
                // Lire le nombre d'objets
                int count = ois.readInt();
                
                // Lire chaque objet
                for (int i = 0; i < count; i++) {
                    GeoObject obj = (GeoObject) ois.readObject();
                    
                    // Mettre à jour le contrôleur dans l'objet
                    obj.setControleur(this);
                    
                    // Ajouter l'objet
                    objs.add(obj);
                }
                
                // Mettre à jour la vue
                updateObjectsList();
                recalculPoints();
                
                JOptionPane.showMessageDialog(view,
                    "Projet ouvert avec succès: " + fileToOpen.getName(),
                    "Ouverture réussie",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (IOException | ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(view,
                    "Erreur lors de l'ouverture du projet: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
