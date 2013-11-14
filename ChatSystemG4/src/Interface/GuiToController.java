package Interface;

import Model.User;

public interface GuiToController {
	void sendHelloMessage(User user);
	void sendGoodbyeMessage(User localUser);
	void sendTextMessage(User user, String message);
	
}
