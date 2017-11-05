package songfinder;

import java.util.HashMap;

/**
 * A read/write lock that allows multiple readers, disallows multiple writers, and allows a writer to 
 * acquire a read lock while holding the write lock. 
 * 
 * A writer may also acquire a second write lock.
 * 
 * A reader may not upgrade to a write lock.
 * 
 */
public class ReentrantLock {

	private HashMap<Long, Integer> readers;
	private HashMap<Long, Integer> writers;

	/**
	 * Construct a new ReentrantLock.
	 */
	public ReentrantLock() {

		this.readers = new HashMap<Long, Integer>();
		this.writers = new HashMap<Long, Integer>();
	}

	/**
	 * Returns true if the invoking thread holds a read lock.
	 * @return
	 */
	public synchronized boolean hasRead() {
		
		Long threadId = Thread.currentThread().getId();
		
		if (readers.get(threadId) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if the invoking thread holds a write lock.
	 * @return
	 */
	public synchronized boolean hasWrite() {
		
		Long threadId  = Thread.currentThread().getId();
		
		if (writers.get(threadId) != null) {
			return true;
		} 	
		return false;
	}

	/**
	 * Non-blocking method that attempts to acquire the read lock.
	 * Returns true if successful.
	 * @return
	 */
	public synchronized boolean tryLockRead() {
		
		Long threadId = Thread.currentThread().getId();
		
		if (writers.keySet().isEmpty() || writers.containsKey(threadId)) {		
			if (!readers.containsKey(threadId)) {	
				readers.put(threadId, 1);
			} else {
				readers.put(threadId, readers.get(threadId) + 1);
			}
		} else {
			return false;
		}
		return true;		
	}		

	/**
	 * Non-blocking method that attempts to acquire the write lock.
	 * Returns true if successful.
	 * @return
	 */	
	public synchronized boolean tryLockWrite() {
		
		Long threadId = Thread.currentThread().getId();
		
		if(this.hasWrite()) {
			writers.put(threadId, writers.get(threadId) + 1);
			return true;
		}
		if (readers.keySet().isEmpty() && writers.keySet().isEmpty()) {
			 if (!writers.containsKey(threadId)) {	
				 writers.put(threadId, 1);
			 } else {
				 writers.put(threadId, writers.get(threadId) + 1);
			 }	
		} else {
			return false;
		}
		return true;
	}	
	

	 /**
	  * Blocking method that will return only when the read lock has been 
	  * acquired.
	  */	 
	 public synchronized void lockRead() {
		 
		 while (tryLockRead() == false) {
			 try {
				 
				 this.wait();
				 
			 } catch (InterruptedException ie) {
				ie.printStackTrace();
				System.out.println("error!");
			 }
		 }
	 }
	 
	 /**
	  * Releases the read lock held by the calling thread. Other threads may continue
	  * to hold a read lock.
	  */
	 public synchronized void unlockRead() throws IllegalMonitorStateException {
		 
		 Long threadId = Thread.currentThread().getId();
		 
		 if (!readers.containsKey(threadId)) {
			 throw new IllegalMonitorStateException();
		 }
		 
		 if (readers.get(threadId) == 1) {
			 readers.remove(threadId);
		 } else { 
			 readers.put(threadId, readers.get(threadId) - 1);
		 }
		 this.notifyAll();
	 }

	 /**
	  * Blocking method that will return only when the write lock has been 
	  * acquired.
	  */
	 public synchronized void lockWrite() {
		
		while (tryLockWrite() == false) {
			 try {
				 
				 this.wait();
				 
			 } catch (InterruptedException ie) {
				ie.printStackTrace();
				System.out.println("error!");
			 }	
		}
	 }

	 /**
	  * Releases the write lock held by the calling thread. The calling thread may continue to hold
	  * a read lock.
	  */
	 public synchronized void unlockWrite() throws IllegalMonitorStateException {
	
		 Long threadId = Thread.currentThread().getId();
		 
		 if (!writers.containsKey(threadId)) {
			 throw new IllegalMonitorStateException();
		 }
		 
		 if (writers.get(threadId) == 1) {
			 writers.remove(threadId);
		 } else {
			 writers.put(threadId, writers.get(threadId) - 1);
		 }
		 this.notifyAll();
	 }
}