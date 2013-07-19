package ujf.verimag.bip.java.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SyncComponent extends Component {
	
	private Set<ReceivePort> receivePorts;
	

	private Map<ReceivePort, List<Integer>> notifications; 
	
	private ReceivePort currentNotifiedPort;
	
	
	public SyncComponent(Compound compound) {
		super(compound);
		receivePorts = new HashSet<ReceivePort>();
		compound.getSyncComponents().add(this);
		notifications = new HashMap<ReceivePort, List<Integer>>();
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
	
	
	

	private void updateNotifications() {
		int indexTransitionEnabledBottom = currentNotifiedPort.getComponentBottom().getCurrentIndexNotified();
		if(notifications.containsKey(currentNotifiedPort)) {
			notifications.get(currentNotifiedPort).add(indexTransitionEnabledBottom);
		}
		else {
			List<Integer> indicesTransitionEnabledBottom = new LinkedList<Integer>();
			indicesTransitionEnabledBottom.add(indexTransitionEnabledBottom);
			notifications.put(currentNotifiedPort, indicesTransitionEnabledBottom);
		}
	}

	public synchronized void updateSynced(ReceivePort rcvPort) {
		currentNotifiedPort = rcvPort;
		updateNotifications();
		
		updateTransitionsEnabled();
		
		for(TransitionEnabled transitionEnabled: currentTransitionsEnabled) {
			TransitionSyncComponent transition = transitionEnabled.getTransition();
			for(ReceivePort receivePort: transition.getReceivePorts()) {
				receivePort.getComponentBottom().setIndexTransitionEnabled(transitionEnabled.getBottomIndex(receivePort));
				
				if(transition.guard()) {
					allTransitionsEnabled.add(transitionEnabled);
					transition.upAction();
					if(transition.getSendPort() != null) transition.getSendPort().setSynced();
				}
			}
		}
	}
	
	private void updateTransitionsEnabled() {
		for(AbstractTransition t: currentLocation.getOutgoingTransition()) {
			TransitionSyncComponent transition = (TransitionSyncComponent) t; 
			
			if(transition.getIndexReceivePort(currentNotifiedPort) == -1) continue;

			if(isPartialEnable(transition)) {
				updateTransitionsEnabled(transition);
			}		
		}
	}
	
	
	
	private void updateTransitionsEnabled(TransitionSyncComponent transition) {
		currentTransitionsEnabled = new LinkedList<TransitionEnabled>();
		TransitionEnabled t = new TransitionEnabled(transition);
		constructTransitionEnabled(transition, 0, t);
	}
	
	
	/**
	 * 
	 * @param transition
	 * @param indexReceivePort
	 * @param transitionEnabled
	 */
	private void constructTransitionEnabled(TransitionSyncComponent transition, 
			int indexReceivePort, 
			TransitionEnabled transitionEnabled) {
		if(indexReceivePort == transition.getReceivePorts().length) {
			currentTransitionsEnabled.add(transitionEnabled);
			return; 
		}
				
		ReceivePort rcvPort = transition.getReceivePorts()[indexReceivePort];
		if(rcvPort.equals(currentNotifiedPort)) {
			TransitionEnabled transitionEnabledTmp = new TransitionEnabled(transitionEnabled);
			transitionEnabledTmp.getBottomIndices().add(rcvPort.getComponentBottom().getCurrentIndexNotified());
			constructTransitionEnabled(transition, indexReceivePort + 1, transitionEnabledTmp);
		}
		else {
			int countReceivedPort = notifications.get(rcvPort).size();

			for(int i = 0; i < countReceivedPort; i++) {
				TransitionEnabled transitionEnabledTmp = new TransitionEnabled(transitionEnabled);
				transitionEnabledTmp.getBottomIndices().add(rcvPort.getComponentBottom().getCurrentIndexNotified());
				constructTransitionEnabled(transition, indexReceivePort + 1, transitionEnabledTmp);
				
			}
		}
	}



	
	private boolean isPartialEnable(TransitionSyncComponent t) {
		for(ReceivePort receivePort: t.getReceivePorts()) 
			if(!receivePort.getSynced()) return false;
		return true;
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
		int size = currentTransitionsEnabled.size();
		int i;
		for(i = 0 ; i < size; i++) {
			if(currentTransitionsEnabled.get(i).getTransition().getSendPort() == null || 
					currentTransitionsEnabled.get(i).getTransition().getSendPort().getReceivePorts().size() == 0)
				return i; 
		}
		return -1; 
	}
	

	

}
