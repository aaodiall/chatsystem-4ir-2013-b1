	package chatsystemg5.brain;

import java.util.Observable;

public abstract class ChatModel extends Observable {
    
    public ChatModel (){
    }

    public abstract void getState();
    
    public abstract void setState();
    
    public abstract void subjectState();
    
}
