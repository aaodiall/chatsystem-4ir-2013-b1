/**
 * 
 */
package chatSystemIHMs;


import javax.swing.JFileChooser;

/**
 * @author Alpha DIALLO & Joanna VIGNE
 *
 */
public class InterfaceDialogFile {
	private static JFileChooser dialogue ;
	
	/**
	 * create the application
	 */
	public InterfaceDialogFile(){
		// Boîte de sélection de fichier à partir du répertoire
        // "home" de l'utilisateur
        	
		// création de la boîte de dialogue
		dialogue = new JFileChooser();		
	}
	/**
	 * returns the JFileChosser which is opened when the user wants to send a file 
	 * @return dialogue
	 * 			
	 */
	public static JFileChooser getDialogue() {
		return dialogue;
	}
	/**
	 * sets the JFileChooser which is opened when the user wants to send a file 
	 * @param dialogue
	 */
	public static void setDialogue(JFileChooser dialogue) {
		InterfaceDialogFile.dialogue = dialogue;
	}
	
}