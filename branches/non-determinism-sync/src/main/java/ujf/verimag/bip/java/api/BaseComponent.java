package ujf.verimag.bip.java.api;

import java.util.concurrent.Semaphore;

import ujf.verimag.bip.java.engine.Engine;
import ujf.verimag.bip.java.exceptions.EngineException;


public abstract class BaseComponent extends Component implements Runnable {
	private Thread thread;
	private Engine engine; 
	
	/**
	 * This semaphore is used to alternate execution between the engine and base components. 
	 */
	private final Semaphore semaphore = new Semaphore(0);
	
	private SendPort portFired; 
	
	
	
	public BaseComponent(Compound compound) {
		super(compound);
		compound.getBaseComponents().add(this);
		thread = new Thread(this);
	}
	
	public void setSynced() {
		for(AbstractTransition transition : currentLocation.getOutgoingTransition()) {
			if(transition.guard())
				transition.getSendPort().setSynced();
		}
	}
	
	public void setEngine(Engine engine) {
		this.engine = engine; 
	}
	
	public AbstractTransition getTransitionCurrentLocation(SendPort port) {
		for(AbstractTransition t: currentLocation.getOutgoingTransition()) {
			if(t.getSendPort().equals(port))
				return t;
		}
		return null;
	}

	
	public void addTransition(Transition trans) {
		transitions.add(trans);
	}
	

	
	public void notifyEngine() {
		setSynced();
		engine.notified();
	}

	private void performTransition() {
		if(portFired != null) {
			AbstractTransition transition = getTransitionCurrentLocation(portFired);
			if(transition == null) {
				throw new EngineException("The selected port by the engine is not possible from the current state.");
			}
			else {
				//TODO check destination not equal to null, otherwise there is a local deadlock
				setCurrentLocation(transition.getDestination());
				transition.action();
			}
			portFired = null; // not needed but for safety. 
		}
	}
	
	
	public void execute(SendPort port) {
		portFired = port; 
		semaphore.release();
	}
	/**
	 * Loop for ever (return from the loop when receiving an interrupt from the engine, in case of a global deadlock):
	 *	A. First we notify the engine, that is:
	 *		A.1. call of setSynced method. This method checks the guard of each outgoing transition and calls setSynced on the send port 
	 *		if the corresponding guard is true. 
	 *		A.2. Calling setSynced on the send port implies a call of setSynced on all its corresponding receive ports. 
	 *		A.3. Calling a setSynced on a receive port will set its synced value to true and update the synced of the corresponding sync component
	 *		{@link #updateSynced()} and the calls propagate recursively up to the top sync components (see. {@link #updateSynced()}). 
	 *		A.4. notifies the engine, increment the value of the engine's semaphore. 
	 *	B. Wait on the semaphore of the current base component. 
	 *		B.1. The engine selects a top sync component to be executed and compute the send port to be executed by each base component, 
	 *		B.2. Then, the engine notifies all the base components by setting the port to be executed and releasing their semaphores.
	 *	C. Each base component checks the port p to be executed:
	 *		C.1. if p is equal null, nothing to be done. 
	 *		C.2. otherwise, execute the corresponding action of the transition.
	 */
	public void run() {
		while(true) {
			try {
				notifyEngine();
				semaphore.acquire();
			}
			catch (InterruptedException e) {
				// An interrupt has been sent from the engine, in case of global deadlock. 
				return;
			}
			performTransition();
		}
	}
	

	
	public Thread getThread() {
		return thread; 
	}
	
}
