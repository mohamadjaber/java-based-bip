package ujf.verimag.bip.java.api;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ujf.verimag.bip.java.types.WrapType;


public class SendPort  {
	
	private List<WrapType<?>> variables;
	private Component component;
	
	private Set<ReceivePort> receivePorts;
	
	public SendPort(Component component, WrapType<?>... vars) {
		this.component = component; 
		this.component.addSendPort(this);
		variables = new ArrayList<WrapType<?>>();
		for(WrapType<?> o: vars) {
			this.variables.add(o);
		}
		
		receivePorts = new HashSet<ReceivePort>();
	}
	
	
	public synchronized void setSynced() {
		for(ReceivePort rcvPort: receivePorts) {
			rcvPort.setSynced();
		}
	}

	
	/**
	 * 
	 */
	public void conflictReset() {
		assert(component instanceof BaseComponent);
		BaseComponent baseComponent = (BaseComponent) component; 
		for(AbstractTransition t : baseComponent.getCurrentLocation().getOutgoingTransition()) {
			SendPort sendPort = t.getSendPort();
			for(ReceivePort rcvPort: sendPort.receivePorts) {
				rcvPort.getSyncComponent().upReset(sendPort);
			}
		}
	}
	

		
	public WrapType<?> getVariable(int index) {
		return variables.get(index);
	}
	
	public Set<ReceivePort> getReceivePorts() {
		return receivePorts;
	}
	
	public Component getComponent() {
		return component; 
	}
	


}
