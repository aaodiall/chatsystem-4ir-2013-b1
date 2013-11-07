package chatSystem.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RemoteSystems extends Model{
	
	private static RemoteSystems instance;
	private Map<String,RemoteSystemInformation> remoteSystemsInformation;
	
	private RemoteSystems () {
		this.remoteSystemsInformation = new HashMap<String, RemoteSystemInformation>();
	}

	public void addRemoteSystem(RemoteSystemInformation newRS) {
	}
	
	public void deleteRemoteSystem(String idRemoteSystem) {
	}
	
	public List<String> getUserList() {
		List<String> userList = new ArrayList<String>();
		Set<String> remoteSystems = this.remoteSystemsInformation.keySet();
		for(String rs: remoteSystems) {
			userList.add(this.remoteSystemsInformation.get(rs).getUsername());
		}
		return userList;
	}
	
	public static RemoteSystems getInstance() {
		if (instance == null) {
			instance = new RemoteSystems();
		}
		return instance;
	}
	
}
