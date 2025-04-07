/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package geoanalytique.gui;

import geoanalytique.view.GeoAnalytiqueView;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JPanel;

/**
 * Classe representant l'interface graphique principale.
 * 
 * 
 */
public class GeoAnalytiqueGUI extends JPanel {

   private GeoAnalytiqueView grille;
    
   
   public GeoAnalytiqueGUI() {
       grille = new GeoAnalytiqueView();
       
       // TODO: a modifier!!!
       this.setLayout(new BorderLayout());
       this.add(grille);
       // TODO: a completer
   }
   
   public GeoAnalytiqueView getCanvas() {
    	return grille;
    }
    
    public Container getPanelElements() {
    	// TODO: a completer
        return null;
    }
    
    public Container getPanelIDs() {
    	// TODO: a completer
        return null;
    }
    
    public Container getPanelOperations() {
    	// TODO: a completer
        return null;
    }

    
    
    
}
