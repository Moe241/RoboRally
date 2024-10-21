
package dk.dtu.compute.se.pisd.designpatterns.observer;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * This is the subject of the observer design pattern roughly following
 * the definition of the GoF.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public abstract class Subject {

	public Set<Observer> getObservers() {
		return observers;
	}

	public void setObservers(Set<Observer> observers) {
		this.observers = observers;
	}

	private Set<Observer> observers =
			Collections.newSetFromMap(new WeakHashMap<>());


	/**
	 * This methods allows an observer to register with the subject
	 * for update notifications when the subject changes.
	 *
	 * @param observer the observer who registers
	 */
	final public void attach(Observer observer) {
		observers.add(observer);
	}

	/**
	 * This methods allows an observer to unregister from the subject
	 * again.
	 *
	 * @param observer the observer who unregisters
	 */
	final public void detach(Observer observer) {
		observers.remove(observer);
	}

	/**
	 * This method must be called from methods of concrete subclasses
	 * of this subject class whenever its state is changed (in a way
	 * relevant for the observer).
	 */
	final protected void notifychange() {
		for (Observer observer: observers) {
			observer.update_M(this);
		}
	}

}