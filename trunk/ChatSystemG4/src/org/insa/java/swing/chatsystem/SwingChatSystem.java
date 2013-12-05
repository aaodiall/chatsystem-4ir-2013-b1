package org.insa.java.swing.chatsystem;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.insa.java.chatsystem.JavaChatSystem;
import org.insa.java.swing.view.SwingChatGUI;

public class SwingChatSystem extends JavaChatSystem {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new SwingChatGUI();
	}
}
