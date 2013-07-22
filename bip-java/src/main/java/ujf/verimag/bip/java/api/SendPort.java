package ujf.verimag.bip.java.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ujf.verimag.bip.java.types.WrapType;


public class SendPort  {
	
	private List<WrapType> variables;
	private Component component;
	
	private Set<ReceivePort> receivePorts;
	
	public SendPort(WrapType... vars) {
		variables = new ArrayList<WrapType>();
		for(WrapType o: vars) {
			this.variables.add(o);
		}
		
		receivePorts = new HashSet<ReceivePort>();
	}
	
	
	public void setSynced() {
		for(ReceivePort rcvPort: receivePorts) {
			rcvPort.setSynced();
		}
	}
	
	
	/**
	 * 
	 */
	public void conflictReset() {
		if(component instanceof BaseComponent) {
			BaseComponent baseComponent = (BaseComponent) component; 
			for(AbstractTransition t : baseComponent.getCurrentLocation().getOutgoingTransition()) {
				if(!t.getSendPort().equals(this)) {
					t.getSendPort().reset();
				}
			}
		}
	}
	
	
	/**
	 * 
	 */
	private void reset() {
		for(ReceivePort receivePort: receivePorts) {
			SyncComponent syncComponent = receivePort.getSyncComponent();
			TransitionSyncComponent transition = syncComponent.getCurrentTransition();
			if(transition != null && Arrays.asList(transition.getReceivePorts()).contains(receivePort)) {
				syncComponent.reset();
				transition.getSendPort().reset();
			}
		}
	}
	
		
	public WrapType getVariable(int index) {
		return variables.get(index);
	}
	
	public void setComponent(Component component) {
		this.component = component; 
		this.component.addSendPort(this);
	}
	
	public Set<ReceivePort> getReceivePorts() {
		return receivePorts;
	}
	
	public Component getComponent() {
		return component; 
	}
	
}
