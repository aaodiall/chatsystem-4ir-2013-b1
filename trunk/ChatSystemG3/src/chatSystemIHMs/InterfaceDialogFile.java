/**
 * 
 */
package chatSystemIHMs;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

/**
 * @author alpha
 *
 */
public class InterfaceDialogFile {
	private static JFileChooser dialogue ;
	public InterfaceDialogFile(){
		// Boîte de sélection de fichier à partir du répertoire
        // "home" de l'utilisateur
        
            // création de la boîte de dialogue
            dialogue = new JFileChooser();
          
		
	}
	public static JFileChooser getDialogue() {
		return dialogue;
	}
	public static void setDialogue(JFileChooser dialogue) {
		InterfaceDialogFile.dialogue = dialogue;
	}
	
}