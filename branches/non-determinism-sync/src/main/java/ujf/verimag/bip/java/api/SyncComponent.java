package ujf.verimag.bip.java.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

import ujf.verimag.bip.java.types.WrapType;

@SuppressWarnings("rawtypes")
public abstract class SyncComponent extends Component {
	
	private Set<ReceivePort> receivePorts;

	private Map<ReceivePort, List<Integer>> notifications; 
	
	private ReceivePort currentNotifiedPort;
	private int currentBottomIndex;

	
	private int indexToExecute;
	
	private List<TransitionEnabled> topTransitionsEnabled;

	private Semaphore semaphore;
	
	public SyncComponent(Compound compound) {
		
		super(compound);
		receivePorts = new HashSet<ReceivePort>();
		compound.getSyncComponents().add(this);
		notifications = new HashMap<ReceivePort, List<Integer>>();
		indexToExecute = -1;
		currentBottomIndex = -1;
		topTransitionsEnabled = new LinkedList<TransitionEnabled>();
		semaphore = new Semaphore(1);
	}
	
	public Set<ReceivePort> getReceivePort() {
		return receivePorts;
	}
	
	public void reset() {
		for(ReceivePort rcvPort : receivePorts) {
			rcvPort.reset();
		}
		notifications.clear();
		topTransitionsEnabled.clear();
		allTransitionsEnabled.clear();
		indexToExecute = -1; 
		resetCopyVariables();
	}
	
	private void resetCopyVariables() {
		for(WrapType var: variables) {
			var.reset();
		}
	}
	
	/**
	 * An up reset that comes from a base component
	 * @param sendPort
	 */
	public void upReset(SendPort sendPort) {
		for(int i = 0; i < allTransitionsEnabled.size(); i++) {
			TransitionEnabled transitionEnabled = allTransitionsEnabled.get(i);
			TransitionSyncComponent transition = transitionEnabled.getTransition();	
			
			for(ReceivePort rcvPort: transition.getReceivePorts()) {
				if(rcvPort.getComponentBottom().equals(sendPort.getComponent()) && !rcvPort.getSendPort().equals(sendPort)) {
					transitionEnabled.disable();
					if(transition.getSendPort() != null) {
						SyncComponent upSyncComponent = (SyncComponent) transition.getSendPort().getComponent();
						upSyncComponent.upReset(this, i);
					}
				}		
			}
		}
	}
	
	
	
	
	/**
	 * A reset that comes from a sync component
	 * @param indexExecutedBottom
	 */
	public void upReset(SyncComponent component, int indexExecutedBottom) {
		for(int i = 0; i < allTransitionsEnabled.size(); i++) {
			TransitionEnabled transitionEnabled = allTransitionsEnabled.get(i);
			TransitionSyncComponent transition = transitionEnabled.getTransition();	
			for(ReceivePort rcvPort: transition.getReceivePorts()) {
				if(rcvPort.getComponentBottom().equals(component) && transitionEnabled.getBottomIndex(rcvPort) != indexExecutedBottom) {
					transitionEnabled.disable();
					if(transition.getSendPort() != null) {
						SyncComponent upSyncComponent = (SyncComponent) transition.getSendPort().getComponent();
						upSyncComponent.upReset(this, i);
					}
				}		
			}
		}
	}
	
	
	
	
	public void addTransition(TransitionSyncComponent trans) {
		transitions.add(trans);
	}
	

	private void updateNotifications() {
		if(notifications.containsKey(currentNotifiedPort)) {
			notifications.get(currentNotifiedPort).add(currentBottomIndex);
		}
		else {
			List<Integer> indicesTransitionEnabledBottom = new LinkedList<Integer>();
			indicesTransitionEnabledBottom.add(currentBottomIndex);
			notifications.put(currentNotifiedPort, indicesTransitionEnabledBottom);
		}
	}
	

	public void updateSynced(ReceivePort rcvPort, int indexBottom) {	
		acquireSemaphore();
		currentNotifiedPort = rcvPort;
		currentBottomIndex = indexBottom; 
			
		updateNotifications();

		currentTransitionsEnabled.clear();
		
		updateTransitionsEnabled();
		
		Map<SendPort, Integer> upSendPortIndex = new HashMap<SendPort, Integer>();
		
		for(TransitionEnabled transitionEnabled: currentTransitionsEnabled) {
			TransitionSyncComponent transition = transitionEnabled.getTransition();
			
			transitionEnabled.acquireBottomSemaphores();
			transitionEnabled.updateBottomIndices();
			
			if(transition.guard()) {
				allTransitionsEnabled.add(transitionEnabled);
				
				int currentIndex = allTransitionsEnabled.size() - 1;

				setIndexTransitionEnabled(currentIndex);
				transition.upAction();
				transitionEnabled.releaseBottomSemaphores();

				if(transition.getSendPort() != null) {
					upSendPortIndex.put(transition.getSendPort(), currentIndex);
				}			
			}
			else {
				transitionEnabled.releaseBottomSemaphores();
			}
		}
		releaseSemaphore();
		
		for(SendPort sendPort: upSendPortIndex.keySet())
			sendPort.setSynced(upSendPortIndex.get(sendPort));
	}
	
	
	public void updateSynced1(ReceivePort rcvPort, int indexBottom) {	
		acquireSemaphore();
		currentNotifiedPort = rcvPort;
		currentBottomIndex = indexBottom; 
			
		updateNotifications();

		currentTransitionsEnabled.clear();
		
		updateTransitionsEnabled();
		
		for(TransitionEnabled transitionEnabled: currentTransitionsEnabled) {
			TransitionSyncComponent transition = transitionEnabled.getTransition();
			
			transitionEnabled.acquireBottomSemaphores();
			transitionEnabled.updateBottomIndices();
			
			if(transition.guard()) {
				allTransitionsEnabled.add(transitionEnabled);
				
				int currentIndex = allTransitionsEnabled.size() - 1;

				setIndexTransitionEnabled(currentIndex);
				transition.upAction();
				transitionEnabled.releaseBottomSemaphores();

				if(transition.getSendPort() != null) 	
					transition.getSendPort().setSynced(currentIndex);
			
			}
			else {
				transitionEnabled.releaseBottomSemaphores();
			}
		}
		releaseSemaphore();
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
	
	
	private boolean isPartialEnable(TransitionSyncComponent transition) {		
		for(ReceivePort receivePort: transition.getReceivePorts()) 
			if(!notifications.containsKey(receivePort)) return false;
		return true; 
	}

	
	
	private void updateTransitionsEnabled(TransitionSyncComponent transition) {
		TransitionEnabled t = new TransitionEnabled(transition, this);
		constructTransitionEnabled(transition, 0, t);
	}
	
	
	/**
	 * 
	 * @param transition
	 * @param indexReceivePort
	 * @param transitionEnabled
	 */
	private void constructTransitionEnabled(TransitionSyncComponent transition, int indexReceivePort, TransitionEnabled transitionEnabled) {
		if(indexReceivePort == transition.getReceivePorts().length) {
			currentTransitionsEnabled.add(transitionEnabled);
			return; 
		}
		
		ReceivePort rcvPort = transition.getReceivePorts()[indexReceivePort];
		if(rcvPort.equals(currentNotifiedPort)) {
			TransitionEnabled transitionEnabledTmp = new TransitionEnabled(transitionEnabled);
			transitionEnabledTmp.setBottomIndex(indexReceivePort,  currentBottomIndex);
			constructTransitionEnabled(transition, indexReceivePort + 1, transitionEnabledTmp);
		}
		else {		
			int countReceivedPort = notifications.get(rcvPort).size();

			for(int i = 0; i < countReceivedPort; i++) {
				TransitionEnabled transitionEnabledTmp = new TransitionEnabled(transitionEnabled);
				transitionEnabledTmp.setBottomIndex(indexReceivePort,  notifications.get(rcvPort).get(i));
				constructTransitionEnabled(transition, indexReceivePort + 1, transitionEnabledTmp);
			}
		}
	}


	
	
	private boolean checkIndex(int index) {
		if(indexToExecute != -1) {
			if(index != indexToExecute) {
				System.out.println("Conflict data detection");
				System.exit(0);
			} else { // We allow conflict on the same index, but possible data override. 
				System.out.println("Warning.");
				return false;
			}
		}
		return true;
	}
	
	
	private void updateOriginalValues() {
		for(WrapType variable: variables) {
			variable.updateOriginalValue();
		}
	}
	
	public void propagateEnablePorts(Map<BaseComponent, SendPort> componentEnablePort, int index) {
	
		if(!checkIndex(index)) return; // index already executed
	
		indexToExecute = index; 
		TransitionEnabled transitionEnabled = allTransitionsEnabled.get(indexToExecute);
		TransitionSyncComponent transition = transitionEnabled.getTransition();
		
		setIndexTransitionEnabled(indexToExecute);
		transitionEnabled.updateBottomIndices();	
		updateOriginalValues();
		transition.action();
		updateOriginalValues();

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
				if(componentEnablePort.get(component) != null) { // conflict detection
					
					// Allows conflict on the same port
					if(componentEnablePort.get(component).equals(sendPort)) return; 
					
					System.out.println(sendPort + " <--> " + componentEnablePort.get(component));
					System.out.println("DAG detection - Component" + component + " has been notified two times");
					System.exit(0);
				}
				BaseComponent baseComponent = (BaseComponent) component;
				componentEnablePort.put(baseComponent, sendPort);
				sendPort.conflictReset();
			}
			else {
				((SyncComponent) component).propagateEnablePorts(componentEnablePort, transitionEnabled.getBottomIndex(receivePorts[i]));
			}
		}
	}
	
	
	public boolean isTopEnabled() {
		setTopTransitionsEnabled();
		return topTransitionsEnabled.size() > 0; 
	}
	
	
	public int getRandomTopTransitionEnable() {
		return (int) (Math.random() * topTransitionsEnabled.size());
	}
	
	
	private void setTopTransitionsEnabled() {
		topTransitionsEnabled = new LinkedList<TransitionEnabled>();
		for(TransitionEnabled t : allTransitionsEnabled) {
			if(t.isEnabled() && (t.getTransition().getSendPort() == null || t.getTransition().getSendPort().getReceivePorts().size() == 0))
				topTransitionsEnabled.add(t); 
		}
	}
	
	public void acquireSemaphore() {
		try {
			semaphore.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void releaseSemaphore() {
		semaphore.release();
	}
	



}
