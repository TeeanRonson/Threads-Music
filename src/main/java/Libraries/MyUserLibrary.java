package Libraries;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gson.JsonObject;

import BaseObjects.SingleUserInfo;
import Generics.ReentrantLock;


public class MyUserLibrary {

	private HashMap<String, SingleUserInfo> userInfo; 
	private ReentrantLock rwl;
	
	public MyUserLibrary() {
		this.userInfo = new HashMap<String, SingleUserInfo>();
		this.rwl = new ReentrantLock();
	}
	
	public void addNewUser(SingleUserInfo object) {
		rwl.lockWrite();
		newUserInfo(object);
		rwl.unlockWrite();
	}
	
	private void newUserInfo(SingleUserInfo object) {
		
		if (!this.userInfo.containsKey(object.getUsername())) {
			this.userInfo.put(object.getUsername(), object);
		
		}
	}
	
	public boolean verifyUser(String username, String password) {
		
		try { 
			rwl.lockRead();
		
			if (this.userInfo.get(username) != null) {
				String passwordCheck = this.userInfo.get(username).getPassword();
			
				if (passwordCheck.equals(password)) { 
					return true;
				}
			}
			return false;
		} finally {
			rwl.unlockRead();
		}
	}
	
	public JsonObject getHistory(String username) {
			
		try { 
			rwl.lockRead();
		
			JsonObject result = new JsonObject();
			
			result = this.userInfo.get(username).toJson();
			
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	public String getLastLogin(String username) {
		
		try { 
			rwl.lockRead();
			
			String lastLogin = this.userInfo.get(username).getLastLogin().toString();
			
			
			return lastLogin;
		} finally { 
			rwl.unlockRead();
		}
		
	}
	
	public void setNewLogin(String username, String newTime) {
		
		rwl.lockWrite();
		SingleUserInfo user = this.userInfo.get(username);
		user.setNewLogin(newTime);
		rwl.unlockWrite();
		
	}
	
	public void setLastLogin(String username) {
		
		rwl.lockWrite();
		SingleUserInfo user = this.userInfo.get(username);
		user.setLastLogin();
		rwl.unlockWrite();
		
	}
	
	public void addToHistory(String username, String query) {
		
		rwl.lockWrite();
		LinkedList<String> result = this.userInfo.get(username).getHistory();
		result.add(query);
		rwl.unlockWrite();
		
	}
	
	public void clearHistory(String username) {
		
		rwl.lockWrite();
		String password = this.userInfo.get(username).getPassword();
		String lastLogin = this.userInfo.get(username).getLastLogin();
		String newLogin = this.userInfo.get(username).getNewLogin();
		LinkedList<String> newHistory = new LinkedList<String>();
		SingleUserInfo replace = new SingleUserInfo(username, password, newHistory, newLogin, lastLogin);
		this.userInfo.put(username, replace);
		rwl.unlockWrite();
		
	}
	
	
		
}












