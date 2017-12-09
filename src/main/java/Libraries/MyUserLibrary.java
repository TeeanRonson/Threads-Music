package Libraries;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import BaseObjects.SingleUserInfo;
import Generics.ReentrantLock;

/**
 * My user Library stores information about users of Threads Music.
 * 
 * Declares private data members
 * @author Rong
 *
 */
public class MyUserLibrary {

	private HashMap<String, SingleUserInfo> userInfo;
	private HashMap<String, Integer> popularArtists;
	private HashMap<String, Integer> popularTitles;
	private ReentrantLock rwl;
	
	/** 
	 * Instantiates private data members
	 */
	public MyUserLibrary() {
		this.userInfo = new HashMap<String, SingleUserInfo>();
		this.popularArtists = new HashMap<String, Integer>();
		this.popularTitles = new HashMap<String, Integer>();
		this.rwl = new ReentrantLock();
	}
	
	/** 
	 * Adds a new SingleUserInfo object to the data structure
	 * @param object
	 */
	public void addNewUser(SingleUserInfo object) {
		rwl.lockWrite();
		newUserInfo(object);
		rwl.unlockWrite();
	}
	
	/** 
	 * Private method saves the username as the key and the 
	 * SingleUserInfo as the value in the HashMap
	 * @param object
	 */
	private void newUserInfo(SingleUserInfo object) {
		
		if (!this.userInfo.containsKey(object.getUsername())) {
			this.userInfo.put(object.getUsername(), object);
		
		}
	}
	
	/** 
	 * Method to verify that the user information exists. 
	 * Returns true if information exists, false otherwise. 
	 * @param username
	 * @param password
	 * @return
	 */
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
	
	/**
	 * Method to return the history of specified user.
	 * @param username
	 * @return
	 */
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
	
	/**
	 * Method to return the last logged in time of the user
	 * @param username
	 * @return
	 */
	public String getLastLogin(String username) {
		
		try { 
			rwl.lockRead();
			
			String lastLogin = this.userInfo.get(username).getLastLogin().toString();
			
			
			return lastLogin;
		} finally { 
			rwl.unlockRead();
		}
		
	}
	
	/**
	 * Method to set a new login time for the user
	 * @param username
	 * @param newTime
	 */
	public void setNewLogin(String username, String newTime) {
		
		rwl.lockWrite();
		SingleUserInfo user = this.userInfo.get(username);
		user.setNewLogin(newTime);
		rwl.unlockWrite();
		
	}
	
	/**
	 * Method to set the last login time of the user 
	 * @param username
	 */
	public void setLastLogin(String username) {
		
		rwl.lockWrite();
		SingleUserInfo user = this.userInfo.get(username);
		user.setLastLogin();
		rwl.unlockWrite();
		
	}
	
	/** 
	 * Method to add a new search to the history list of the user
	 * @param username
	 * @param query
	 */
	public void addToHistory(String username, String query) {
		
		rwl.lockWrite();
		LinkedList<String> result = this.userInfo.get(username).getHistory();
		result.add(query);
		rwl.unlockWrite();
		
	}
	
	/** 
	 * Method that clears the history of the user
	 * @param username
	 */
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
	
	/** 
	 * Method to add trending artists to data structure
	 * @param artist
	 */
	public void addPopularArtist(String artist) {
		rwl.lockWrite();
		HashMap<String, Integer> artists = this.popularArtists;
		if (!artists.containsKey(artist)) {
			artists.put(artist, 1);
		} else { 
			artists.put(artist, artists.get(artist) + 1);
		}
		rwl.unlockWrite();
	}
	
	/**
	 * Method to add trending titles to the data structure
	 * @param title
	 */
	public void addPopularTitle(String title) {
		rwl.lockWrite();
		HashMap<String, Integer> titles = this.popularTitles;
		if (!titles.containsKey(title)) {
			titles.put(title, 1);
		} else { 
			titles.put(title, titles.get(title) + 1);
		}
		rwl.unlockWrite();
	}
	
	/**
	 * Thread Safe method to return all trending artists
	 * @return
	 */
	public JsonArray getPopularArtists() {
		
		try { 
			rwl.lockRead();
			
			JsonArray result = new JsonArray();
			
			HashMap<String, Integer> dummy = sortByValues(this.popularArtists);
			
			int count = 0;
			for (String x: dummy.keySet()) {
				count++;
				result.add(x);
				
				if (count == 10) {
					break;
				}
			}		
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/**
	 * Thread safe method to return all trending titles
	 * @return
	 */
	public JsonArray getPopularTitles() {
		
		try { 
			rwl.lockRead();
			
			JsonArray result = new JsonArray();
					
			HashMap<String, Integer> dummy = sortByValues(this.popularTitles);
			
			int count = 0;
			for (String x: dummy.keySet()) {
				count++;
				result.add(x);
				
				if (count == 10) {
					break;
				}
			}
			return result;
		} finally { 
			rwl.unlockRead();
		}
	}
	
	/**
	 * https://beginnersbook.com/2013/12/how-to-sort-hashmap-in-java-by-keys-and-values/
	 * Source for sort HashMap by values
	 * @param map
	 * @return
	 */
	private static HashMap sortByValues(HashMap map) {
		
		List list = new LinkedList(map.entrySet());
		
		Collections.sort(list, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
			
				return ((Comparable) ((Map.Entry) o2).getValue()).compareTo(((Map.Entry) o1).getValue());
				
			}
		});
		
		HashMap sorted = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sorted.put(entry.getKey(), entry.getValue());
		}
		return sorted;
	}
		
}
