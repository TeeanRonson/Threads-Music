package Generics;

import java.util.LinkedList;

import Utilities.RejectedExecutionException;

/** 
 * WorkQueue class used for Multi-threading
 * Declares private data members
 *  
 * @author Rong
 *
 */
public class WorkQueue {
	private final int myThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    private volatile boolean shutDown;
 
    /**
     * Constructor takes in one input 
     * which determines the number of threads 
     * to be used in building the library 
     * 
     * Initialises private data members
     * 
     * Creates PoolWorker objects up to an amount
     * specified by the number of threads passed 
     * into the constructor 
     * 
     * @param myThreads
     */
    public WorkQueue(int myThreads) {
        this.myThreads = myThreads;
        this.threads = new PoolWorker[myThreads];
        this.queue = new LinkedList();    
        this.shutDown = false;
        
        for (int i = 0; i < myThreads; i++) {
            this.threads[i] = new PoolWorker();
            this.threads[i].start();
        }
    }
 
    /**
     * Public method that executes the object
     * implementing the Runnable interface
     * 
     * Adds each new object to the work queue 
     * and notifies threads
     * @param r
     * @throws RejectedExecutionException 
     */
    public void execute(Runnable r) throws RejectedExecutionException {
    	
    		
    		if (this.shutDown == true) {
    			throw new RejectedExecutionException("Queue is closed");
    		} else {
    			synchronized(queue) {	
    				queue.addLast(r);
    				queue.notify();
    			}
    			
    		}
    }
    
    /** 
     * Public method that when called
     * stops accepting new objects into 
     * the queue 
     */
    public boolean shutDown() {
   
    		this.shutDown = true;
    		
    		return this.shutDown;
    }
    
    /** 
     * Public method that blocks 
     * until all tasks have completed 
     * execution after a shutdown request
     */
    public void awaitTermination() {
    		
    		synchronized (queue) {
    			queue.notifyAll();
    		}	
    	
    		for (int i = 0; i < myThreads; i++) {
    			
    			try {
    				
    				this.threads[i].join();
			
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    		}
   }		
    
    /** 
     * Private class extends from Threads classs
     * @author Rong
     *
     */
    private class PoolWorker extends Thread {
    	
    		/** 
    		 * Public run method that navigates each 
    		 * thread to execute based on certain conditions 
    		 */
        public void run() {
        	
        		Runnable r;

        		while (true) {
        			synchronized(queue) {
        				while(shutDown == false && queue.isEmpty()) {
        					
        					try {
        						queue.wait();
        					}
        					catch (InterruptedException i) {
        					}
        				}
        				
        				if (shutDown == true && queue.isEmpty()) {
        					break;
        				}
        				
        				r = (Runnable) queue.removeFirst();
        				
        			}
        		
        			try {
        				r.run();
        			}
        			catch (RuntimeException e) {
        				e.getMessage();
        				System.out.println("can't run exception");
        			}
        		}
        }
    }
}    