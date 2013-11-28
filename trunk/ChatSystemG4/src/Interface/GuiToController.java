package Interface;

import org.insa.java.model.User;

public interface GuiToController {
	void sendHelloMessage(User user);
	void sendGoodbyeMessage(User localUser);
	void sendTextMessage(User user, String message);
	
}
