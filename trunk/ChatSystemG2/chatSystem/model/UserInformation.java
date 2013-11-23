package chatSystem.model;

public class UserInformation extends Model{
	
	private String username;
	private final String ip;
	protected UserState state;
	
        /**
         * Creation d'une zone d'information sur un utilisateur encore inconnu
         * @param ip adresse ip, seule information disponible
         */
        public UserInformation(String ip) {
            this.ip = ip;
            this.username = null;
            this.state = UserState.DISCONNECTED;
        }
        
        /**
         * Creation d'une zone d'information sur un utilisateur dont on vient d'apprendre l'existence
         * @param username pseudo de l'utilisateur
         * @param ip adresse ip de l'utilisateur
         */
         public UserInformation(String username, String ip) {
            this.ip = ip;
            this.username = username;
            this.state = UserState.CONNECTED;
        }
        
         /**
          * Recuperer le pseudo de l'utilisateur
          * @return username
          */
	public String getUsername() {
		return this.username;
	}
	
        /**
         * Modifier le pseudo de l'utilisateur
         * @param username nouveau pseudo
         */
	public void setUsername(String username) {
                System.out.println("Entering setUsername");
		this.username = username;
                this.setChanged();
                this.notifyObservers(this.username);
                this.clearChanged();
	}
	
        /**
         * Recuperer l'etat de l'utilisateur (connected/disconnected)
         * @return etat de l'utilisateur
         */
	public final UserState getUserState() {
		return this.state;
	}

        /**
         * Mettre a jour l'etat de l'utilisateur (connected/disconnected)
         * @param state nouvel etat
         */
	public void setUserState(UserState state) {
                System.out.println("Entering setState : le nouvel état est : "+state.toString());
		this.state = state;
                this.setChanged();
                this.notifyObservers(this.state);
                this.clearChanged();
	}
	
        /**
         * Recuperer l'adresse ip de l'utilisateur
         * @return adresse ip
         */
	public String getIP() {
		return this.ip;
	}
	
        /**
         * Evalue si l'objet en argument est egal a cette instance de UserInformation
         * @param obj objet a comparer
         * @return resultat de la comparaison
         */
        @Override
        public boolean equals(Object obj) {
           if (obj == this)
               return true;
           if (obj instanceof UserInformation) {
               UserInformation aux = (UserInformation) obj;
               return (aux.getUsername().equals(this.username)) && (aux.getIP().equals(this.ip))
                       && (aux.getUserState() == this.state);
           }
           return false;
        }
}
