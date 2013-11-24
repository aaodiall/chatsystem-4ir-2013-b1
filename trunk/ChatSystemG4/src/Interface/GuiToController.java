package Interface;

import org.insa.model.User;

public interface GuiToController {
	void sendHelloMessage(User user);
	void sendGoodbyeMessage(User localUser);
	void sendTextMessage(User user, String message);
	
}
