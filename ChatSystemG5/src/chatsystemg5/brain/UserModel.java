package chatsystemg5.brain;

public class UserModel extends ChatModel {
    
    private String username;
    private Boolean status;
    
    public UserModel (String username) {
        this.username = username;
        status = true;
    }
    
    public String get_username(){
        return this.username;
    }

    @Override
    public void getState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void subjectState() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
