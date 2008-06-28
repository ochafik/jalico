/**
 * 
 */
package com.ochafik.util.listenable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;

/**
 * <p>
 * Group of coordinated Runnable instances that can be started, interrupted and waited for together.
 * </p><p>
 * It provides for a handy join() method, that waits till all runnables finished their execution.
 * </p><p>
 * Once you added as many runnable tasks as needed through the add(Runnable) method,  
 * there are two ways of waiting for the tasks to finish :
 * <ul>
 * <li>call join() in some thread. This will implicitely start the threads if start() was not called yet, and the join() method will not return until all the thread finished their execution
 * </li><li>register some ActionListener instances and call start() (you cannot register listeners after start() was called). Whenever all threads finished their execution, the actionPerformed method of all the listeners will be called.
 * </li>
 * @author ochafik
 *
 */
public final class Threads {
	private final List<Runner> runners = new ArrayList<Runner>();
	private final Semaphore semaphore = new Semaphore(0);
	private boolean fired = false, started = false;

	private List<ActionListener> actionListeners; 

	private class Runner extends Thread {
		private final Runnable runnable;
		public Runner(Runnable runnable) {
			this.runnable = runnable;
		}
		public void run() {
			try {
				runnable.run();
			} finally {
				int nThreads = runners.size();
				if (!actionListeners.isEmpty() && semaphore.tryAcquire(nThreads - 1)) {
					synchronized (this) {
						if (!fired) {
							fireActionPerformed();
							fired = true;
						}
					}
					semaphore.release(nThreads);
				} else {
					semaphore.release();
				}
			}
		}
	}
	
	/**
	 * Add a task that is to be executed in its own thread.
	 * @param runnable task to be executed in its own thread
	 * @return the runnable argument unchanged
	 */
	public synchronized <T extends Runnable> T add(T runnable) {
		if (started)
			throw new IllegalThreadStateException("Cannot add another runnable to " + getClass().getSimpleName() + " after it started !");
		
		runners.add(new Runner(runnable));
		return runnable;
	}
	
	/**
	 * Starts all the threads.
	 * @throws IllegalThreadStateException if the threads were already started.
	 * @throws NoSuchElementException if no runnable were added to this Threads instance.
	 */
	public synchronized void start() {
		if (started)
			throw new IllegalThreadStateException(getClass().getSimpleName() + " already started !");
		
		if (runners.isEmpty())
			throw new NoSuchElementException("No runnable were added to this " + getClass().getSimpleName());
		
		for (Runner t : runners) {
			t.start();
		}
		started = true;
	}
	
	/**
	 * Calls interrupt() on each of the running threads.
	 * @throws IllegalThreadStateException if threads were not started 
	 */
	public synchronized void interrupt() {
		if (!started)
			throw new IllegalThreadStateException(getClass().getSimpleName() + " not started !");
		
		for (Runner t : runners) {
			try {
				t.interrupt();
			} catch (IllegalThreadStateException ex) {
				// t might have finished its execution
				ex.printStackTrace();
			}
		} 
	}
	
	/**
	 * Waits for all runnable to have finished their execution.
	 * Can be called multiple times : after the first time, this method always returns immediately.
	 * If the Threads is not started yet, this method will start it implicitely.
	 * @throws InterruptedException if method interrupt() was called on the thread that is calling this method.
	 */
	public synchronized void join() throws InterruptedException {
		int nThreads = runners.size();
		if (nThreads == 0)
			return;
		
		if (!started)
			start();
		
		semaphore.acquire(nThreads);
		semaphore.release(nThreads);
	}
	
	public enum State {
		NotStarted, Running, Finished, NoRunnables
	}
	
	public synchronized State getState() {
		int nThreads = runners.size();
		if (nThreads == 0)
			return State.NoRunnables;
		
		if (!started)
			return State.NotStarted;
		
		if (semaphore.tryAcquire(nThreads)) {
			semaphore.release(nThreads);
			return State.Finished;
		}
		return State.Running;
	}
	
	/**
	 * Adds a listener that will be notified upon completion of all of the running threads.
	 * Must not be called after a call to start() or join() (would then throw an IllegalThreadStateException).
	 * @param actionListener
	 * @throws IllegalThreadStateException if start() or join() were called before
	 */
	public synchronized void addActionListener(ActionListener actionListener) {
		if (started)
			// We could allow this, but it would be unsafe
			throw new IllegalThreadStateException("Cannot add an action listener to " + getClass().getSimpleName() + " after it started !");
		
		if (actionListeners == null)
			actionListeners = new ArrayList<ActionListener>();
		
		actionListeners.add(actionListener);
	}
	
	private synchronized void fireActionPerformed() {
		ActionEvent a = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
		for (ActionListener l : actionListeners)
			l.actionPerformed(a);
	}
	
}