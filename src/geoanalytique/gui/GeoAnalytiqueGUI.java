/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geoanalytique.gui;

import geoanalytique.view.GeoAnalytiqueView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

/**
 * Classe representant l'interface graphique principale.
 * 
 */
public class GeoAnalytiqueGUI extends JPanel {

   private GeoAnalytiqueView grille;
   private JPanel panelElements;
   private JPanel panelIDs;
   private JPanel panelOperations;
   private JList<String> listObjects;
   private JList<String> listOperations;
   private JButton btnCreatePoint;
   private JButton btnCreateSegment;
   private JButton btnCreateDroite;
   private JButton btnCreateCercle;
   private JButton btnCreatePolygone;
   private JButton btnExecuteOperation;
   private JButton btnSupprimer;
   private JButton btnRenommer;
   
   /**
    * Constructeur de l'interface graphique principale
    */
   public GeoAnalytiqueGUI() {
       // Initialisation de la zone de dessin
       grille = new GeoAnalytiqueView();
       grille.setPreferredSize(new Dimension(600, 500));
       grille.setBackground(Color.WHITE);
       grille.setBorder(BorderFactory.createLineBorder(Color.BLACK));
       
       // Panel principal
       this.setLayout(new BorderLayout(5, 5));
       
       // Panneau de droite (avec onglets)
       JTabbedPane rightPanel = new JTabbedPane();
       
       // Panneau des éléments (formes géométriques à créer)
       panelElements = createElementsPanel();
       rightPanel.addTab("Formes", panelElements);
       
       // Panneau des identifiants (objets créés)
       panelIDs = createIDsPanel();
       rightPanel.addTab("Objets", panelIDs);
       
       // Panneau des opérations
       panelOperations = createOperationsPanel();
       rightPanel.addTab("Opérations", panelOperations);
       
       // Ajout des composants principaux
       this.add(grille, BorderLayout.CENTER);
       this.add(rightPanel, BorderLayout.EAST);
       
       // Panneau du haut avec le titre
       JPanel headerPanel = new JPanel();
       headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
       JLabel titleLabel = new JLabel("GéoAnalytique");
       titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
       headerPanel.add(titleLabel);
       this.add(headerPanel, BorderLayout.NORTH);
       
       // Panneau du bas avec le statut
       JPanel statusPanel = new JPanel();
       statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
       JLabel statusLabel = new JLabel("Prêt");
       statusPanel.add(statusLabel);
       this.add(statusPanel, BorderLayout.SOUTH);
   }
   
   /**
    * Crée le panneau des éléments (formes géométriques à créer)
    * @return Panneau contenant les boutons de création d'objets
    */
   private JPanel createElementsPanel() {
       JPanel panel = new JPanel();
       panel.setLayout(new GridLayout(5, 1, 5, 5));
       panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
       
       btnCreatePoint = new JButton("Créer un Point");
       btnCreateSegment = new JButton("Créer un Segment");
       btnCreateDroite = new JButton("Créer une Droite");
       btnCreateCercle = new JButton("Créer un Cercle");
       btnCreatePolygone = new JButton("Créer un Polygone");
       
       panel.add(btnCreatePoint);
       panel.add(btnCreateSegment);
       panel.add(btnCreateDroite);
       panel.add(btnCreateCercle);
       panel.add(btnCreatePolygone);
       
       return panel;
   }
   
   /**
    * Crée le panneau des identifiants (objets créés)
    * @return Panneau contenant la liste des objets créés
    */
   private JPanel createIDsPanel() {
       JPanel panel = new JPanel();
       panel.setLayout(new BorderLayout(5, 5));
       panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
       
       listObjects = new JList<>();
       listObjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       JScrollPane scrollPane = new JScrollPane(listObjects);
       scrollPane.setBorder(BorderFactory.createTitledBorder(
               BorderFactory.createEtchedBorder(), 
               "Objets dans le repère", 
               TitledBorder.TOP, 
               TitledBorder.CENTER));
       
       panel.add(scrollPane, BorderLayout.CENTER);
       
       // Panneau pour les boutons d'action sur les objets
       JPanel buttonPanel = new JPanel();
       buttonPanel.setLayout(new GridLayout(1, 2, 5, 5));
       
       btnSupprimer = new JButton("Supprimer");
       btnRenommer = new JButton("Renommer");
       
       buttonPanel.add(btnSupprimer);
       buttonPanel.add(btnRenommer);
       
       panel.add(buttonPanel, BorderLayout.SOUTH);
       
       return panel;
   }
   
   /**
    * Crée le panneau des opérations
    * @return Panneau contenant les opérations disponibles
    */
   private JPanel createOperationsPanel() {
       JPanel panel = new JPanel();
       panel.setLayout(new BorderLayout(5, 5));
       panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
       
       listOperations = new JList<>();
       listOperations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       JScrollPane scrollPane = new JScrollPane(listOperations);
       scrollPane.setBorder(BorderFactory.createTitledBorder(
               BorderFactory.createEtchedBorder(), 
               "Opérations disponibles", 
               TitledBorder.TOP, 
               TitledBorder.CENTER));
       
       panel.add(scrollPane, BorderLayout.CENTER);
       
       btnExecuteOperation = new JButton("Exécuter l'opération");
       
       panel.add(btnExecuteOperation, BorderLayout.SOUTH);
       
       return panel;
   }
   
   /**
    * Retourne la zone de dessin
    * @return Zone de dessin
    */
   public GeoAnalytiqueView getCanvas() {
    	return grille;
    }
    
   /**
    * Retourne le panneau des éléments
    * @return Panneau des éléments
    */
    public Container getPanelElements() {
    	return panelElements;
    }
    
    /**
     * Retourne le panneau des identifiants
     * @return Panneau des identifiants
     */
    public Container getPanelIDs() {
    	return panelIDs;
    }
    
    /**
     * Retourne le panneau des opérations
     * @return Panneau des opérations
     */
    public Container getPanelOperations() {
    	return panelOperations;
    }
    
    /**
     * Retourne le bouton de création de point
     * @return Bouton de création de point
     */
    public JButton getBtnCreatePoint() {
        return btnCreatePoint;
    }
    
    /**
     * Retourne le bouton de création de segment
     * @return Bouton de création de segment
     */
    public JButton getBtnCreateSegment() {
        return btnCreateSegment;
    }
    
    /**
     * Retourne le bouton de création de droite
     * @return Bouton de création de droite
     */
    public JButton getBtnCreateDroite() {
        return btnCreateDroite;
    }
    
    /**
     * Retourne le bouton de création de cercle
     * @return Bouton de création de cercle
     */
    public JButton getBtnCreateCercle() {
        return btnCreateCercle;
    }
    
    /**
     * Retourne le bouton de création de polygone
     * @return Bouton de création de polygone
     */
    public JButton getBtnCreatePolygone() {
        return btnCreatePolygone;
    }
    
    /**
     * Retourne le bouton d'exécution d'opération
     * @return Bouton d'exécution d'opération
     */
    public JButton getBtnExecuteOperation() {
        return btnExecuteOperation;
    }
    
    /**
     * Retourne la liste des objets
     * @return Liste des objets
     */
    public JList<String> getListObjects() {
        return listObjects;
    }
    
    /**
     * Retourne la liste des opérations
     * @return Liste des opérations
     */
    public JList<String> getListOperations() {
        return listOperations;
    }
    
    /**
     * Retourne le bouton de suppression d'objet
     * @return Bouton de suppression
     */
    public JButton getBtnSupprimer() {
        return btnSupprimer;
    }
    
    /**
     * Retourne le bouton de renommage d'objet
     * @return Bouton de renommage
     */
    public JButton getBtnRenommer() {
        return btnRenommer;
    }
}
