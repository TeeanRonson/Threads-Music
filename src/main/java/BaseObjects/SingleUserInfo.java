package BaseObjects;

import java.time.LocalDateTime;
import java.util.LinkedList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class SingleUserInfo {
	
	private String username; 
	private String password; 
	private LinkedList<String> history;
	private String newLogin;
	private String lastLogin;
	
	public SingleUserInfo(String user, String pass, LinkedList<String> history, String newLogin, String lastLogin) {
		this.username= user;
		this.password = pass; 
		this.history = history;
		this.newLogin = newLogin;
		this.lastLogin = lastLogin;
	}	
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public LinkedList<String> getHistory() {
		return this.history;
	}
	
	public String getNewLogin() {
		return this.newLogin;
	}
	
	public String getLastLogin() {
		return this.lastLogin;
	}
	
	public void setNewLogin(String newTime) {
		this.newLogin = newTime;
	}
	
	public void setLastLogin() {
		this.lastLogin = this.newLogin;
		this.newLogin = null;
	}
	
	public JsonObject toJson() {
		
		JsonObject result = new JsonObject();
		JsonArray history = new JsonArray();
		result.addProperty("username", getUsername());
		result.addProperty("password", getPassword());
		for (String x: this.history) {
			history.add(x);
		}
		result.add("history", history);
		
		return result;
	}
	
}
