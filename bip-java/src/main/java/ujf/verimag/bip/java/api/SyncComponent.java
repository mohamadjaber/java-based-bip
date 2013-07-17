package ujf.verimag.bip.java.api;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class SyncComponent extends Component {
	
	private Set<ReceivePort> receivePorts;
	
	private boolean isEnable; 
	
	/**
	 * This variable is set to the transition selected from the current location. 
	 * Although we consider only deterministic sync component but we allow different outgoing transitions from a location with 
	 * the assumption that the guards of the transitions are mutually exclusive, that is if one guard is true, for sure the others
	 * are not. Or, more generally, if a transition is ready, all the others are not. 
	 * This variable is used to check if the current sync component is in the top. This is done by evaluating the selected transition. 
	 */
	private TransitionSyncComponent currentTransition; 
	
	public SyncComponent(Compound compound) {
		super(compound);
		receivePorts = new HashSet<ReceivePort>();
		compound.getSyncComponents().add(this);
		isEnable = false; 
	}
	
	public Set<ReceivePort> getReceivePort() {
		return receivePorts;
	}
	
	public void reset() {
		isEnable = false;
		for(ReceivePort rcvPort : receivePorts) {
			rcvPort.reset();
		}
		currentTransition = null; 
	}
	
	public void addTransition(TransitionSyncComponent trans) {
		transitions.add(trans);
		Location from = trans.getOrigin();
		Location to = trans.getDestination();
		SendPort sendPort = trans.getSendPort();
		ReceivePort[] receivePorts = trans.getReceivePorts();
		
		from.setComponent(this);
		to.setComponent(this);
		if(sendPort != null)
			sendPort.setComponent(this);
		for(ReceivePort rp : receivePorts) {
			rp.setComponent(this);
			this.receivePorts.add(rp);
		}
	}
	
	/**
	 * @deprecated replaced by {@link #updateSynced()}
	 * This method assumes that the behavior of the sync component is full deterministic. That is, 
	 * for each location there exists at maximum one outgoing transition. In the replaced methdo, that is {@link #updateSynced()}, 
	 * we allow multiple outgoing transition, but assuming that the guards of the transitions are mutually exclusive. 
	 */
	public synchronized void updateSyncedBefore() {
		assert(currentLocation.getOutgoingTransition().size() == 1);
		TransitionSyncComponent transition =  (TransitionSyncComponent) currentLocation.getOutgoingTransition().get(0);
		
		ReceivePort[] receivePorts = transition.getReceivePorts();
		
		for(int i = 0; i < receivePorts.length; i++) 
			if(!receivePorts[i].getSynced()) return; 
		
		if(transition.guard()) {
			transition.upAction();
			isEnable = true; 
			if(transition.getSendPort() != null)
				transition.getSendPort().setSynced();
		}
	}
	
	/**
	 * This method is called when a receive port calls synced, that is a receive port is notified to be enable. 
	 * We assume that the SyncComponent are deterministic. This is necessary because otherwise we need to propagate, 
	 * for each up action, a value per each port variable. We cannot take one random enable transition because 
	 * the upper sync component may not be enabled. However, in this method we are taking the first enable transition 
	 * as we assume that it is not possible to have outgoing transitions which are enable at the same time. 
	 * In this case, it will not be a problem even though the upper sync component for this sync component is/are not enable, 
	 * because from the assumption we have the other transition could not be enabled. 
	 */
	public synchronized void updateSynced() {
		for(AbstractTransition t: currentLocation.getOutgoingTransition()) {
			TransitionSyncComponent transition = (TransitionSyncComponent) t; 
			ReceivePort[] receivePorts = transition.getReceivePorts();
			
			for(int i = 0; i < receivePorts.length; i++) 
				if(!receivePorts[i].getSynced()) return; 
			
			if(transition.guard()) {
				transition.upAction();
				isEnable = true; 
				currentTransition = transition; 
				if(transition.getSendPort() != null)
					transition.getSendPort().setSynced();
				break; 
			}
		}
	}
	
	/**
	 * @param componentEnablePort a map that contains a base component and its corresponding send port to be executed.
	 * This methods fill in the map componentEnablePort given as input and executes the down action (action) of the selected 
	 * sync components. After selecting a top sync component to be executed, this method will be called. 
	 * At each step, the method execute the action of the sync component, change the location, and then loop over the receive ports of the current transition. 
	 * For each receive port we take its corresponding send port. Two possible cases: 
	 * 	(1) the sender port is contained in a base component in that case we add the base component and that port to the map;
	 * 	(2) otherwise, we re-call that function (recursive-call) on the sync component of the send port. 
	 */
	public void propagateEnablePorts(Map<BaseComponent, SendPort> componentEnablePort) {
		TransitionSyncComponent transition =  currentTransition;
		transition.action();
		setCurrentLocation(transition.getDestination());
		
		ReceivePort[] receivePorts = transition.getReceivePorts();
		
		for(int i = 0; i < receivePorts.length; i++) {
			SendPort sendPort = receivePorts[i].getSendPort();
			Component component = sendPort.getComponent();
			if(component instanceof BaseComponent) {
				
				/**
				 *  check if the base component has not yet been notified, otherwise DAG detection. 
				 *  The connection should be tree at run-time. 
				 *  otherwise semantic error, a base component receives to execute multiple ports.
				 */
				// 
				if(componentEnablePort.get(component) != null) {
					System.out.println("DAG detection - Component: " + component + " has been notified two times");
					System.exit(0);
				}
				
				componentEnablePort.put((BaseComponent) component, sendPort);
			}
			else {
				((SyncComponent) component).propagateEnablePorts(componentEnablePort);
			}
		}
	}
	
	public boolean isEnable() {
		return isEnable; 
	}
	
	public boolean isTop() {
		return currentTransition != null && currentTransition.getSendPort() == null; 
	}
	
	public TransitionSyncComponent getCurrentTransition() {
		return currentTransition; 
	}
	
	

}
