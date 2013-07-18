package ujf.verimag.bip.java.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SyncComponent extends Component {
	
	private Set<ReceivePort> receivePorts;
	

	private Map<ReceivePort, Integer> notifications; 
	
	
	public SyncComponent(Compound compound) {
		super(compound);
		receivePorts = new HashSet<ReceivePort>();
		compound.getSyncComponents().add(this);
		notifications = new HashMap<ReceivePort, Integer>();
	}
	
	public Set<ReceivePort> getReceivePort() {
		return receivePorts;
	}
	
	public void reset() {
		for(ReceivePort rcvPort : receivePorts) {
			rcvPort.reset();
		}
		notifications.clear();
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
	

	public synchronized void updateSynced(ReceivePort rcvPort) {
		notifications.put(rcvPort, rcvPort.getComponentBottom().getCurrentIndexValues());

		outerLoop:
		for(AbstractTransition t: currentLocation.getOutgoingTransition()) {
			TransitionSyncComponent transition = (TransitionSyncComponent) t; 
			
			for(ReceivePort receivePort: transition.getReceivePorts()) 
				if(!receivePort.getSynced()) continue outerLoop;
			
			
			// all the receive ports have been received for transition t
			
			
			keysValues.add(new KeyValues(t, indexBottom));
			
			if(transition.guard()) {
				transition.upAction();
				if(transition.getSendPort() != null)
					transition.getSendPort().setSynced(keysValues.size());
			}
		}
	}

	
	
	
	public void propagateEnablePorts(Map<BaseComponent, SendPort> componentEnablePort, int index) {
		TransitionSyncComponent transition =  (TransitionSyncComponent) keysValues.get(index).getTransition();
		
		
		
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
					System.out.println("DAG detection - Component" + component + " has been notified two times");
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
		return getIndexEnableTransition() != -1; 
	}
	
	
	public int getIndexEnableTransition() {
		int size = keysValues.size();
		int i;
		for(i = 0 ; i < size; i++) {
			if(keysValues.get(i).getTransition().getSendPort() == null || 
					keysValues.get(i).getTransition().getSendPort().getReceivePorts().size() == 0)
				return i; 
		}
		return -1; 
	}
	

	

}
