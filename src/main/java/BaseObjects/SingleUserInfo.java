package BaseObjects;

import java.time.LocalDateTime;
import java.util.LinkedList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** 
 * SingleUserInfo stores information related to an 
 * individual user
 * @author Rong
 *
 * Declares private data members
 */
public class SingleUserInfo {
	
	private String username; 
	private String password; 
	private LinkedList<String> history;
	private String newLogin;
	private String lastLogin;
	
	/**
	 * Initialises private data members
	 * @param user
	 * @param pass
	 * @param history
	 * @param newLogin
	 * @param lastLogin
	 */
	public SingleUserInfo(String user, String pass, LinkedList<String> history, String newLogin, String lastLogin) {
		this.username= user;
		this.password = pass; 
		this.history = history;
		this.newLogin = newLogin;
		this.lastLogin = lastLogin;
	}	
	
	/** 
	 * Returns the username 
	 * @return
	 */
	public String getUsername() {
		return this.username;
	}
	
	/** 
	 * Returns the password 
	 * @return
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Returns the history of the user
	 * @return
	 */
	public LinkedList<String> getHistory() {
		return this.history;
	}
	
	/**
	 * Returns the new login time
	 * @return
	 */
	public String getNewLogin() {
		return this.newLogin;
	}
	
	/** 
	 * Returns the last login time
	 * @return
	 */
	public String getLastLogin() {
		return this.lastLogin;
	}
	
	/**
	 * Sets a new login time
	 * @param newTime
	 */
	public void setNewLogin(String newTime) {
		this.newLogin = newTime;
	}
	
	/**
	 * Sets the last login time
	 */
	public void setLastLogin() {
		this.lastLogin = this.newLogin;
		this.newLogin = null;
	}
	
	/** 
	 * Returns a JsonObject of all information 
	 * related to the user
	 * @return
	 */
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
