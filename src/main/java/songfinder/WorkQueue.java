package songfinder;

import java.util.LinkedList;

public class WorkQueue {
	private final int myThreads;
    private final PoolWorker[] threads;
    private final LinkedList queue;
    private volatile boolean shutDown;
 
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
 
    public void execute(Runnable r) {
    		if (this.shutDown == false) {
    			synchronized(queue) {
    				queue.addLast(r);
    				queue.notify();
    			}
    		}
    }
    
    public void shutDown() {
   
    		this.shutDown = true;
    }
    
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
    
    private class PoolWorker extends Thread {
    	
        public void run() {
        	
        		Runnable r;

        		while (shutDown == false || !queue.isEmpty()) {
        			synchronized(queue) {
        				if (shutDown == true && queue.isEmpty()) {
        					break;
        				} 
        				while(queue.isEmpty() && shutDown == false) {
        					
                        try {
                            queue.wait();
                        }
                        catch (InterruptedException ignored) {
                        }
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