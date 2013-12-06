package org.insa.java.swing.chatsystem;

import java.util.logging.Level;

import javax.swing.UIManager;

import org.insa.java.chatsystem.JavaChatSystem;
import org.insa.java.swing.view.SwingChatGUI;

import com.sun.istack.internal.logging.Logger;

public class SwingChatSystem extends JavaChatSystem {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			Logger.getLogger(SwingChatSystem.class).log(Level.SEVERE, "", e);
		}
		new SwingChatGUI();
	}
}
