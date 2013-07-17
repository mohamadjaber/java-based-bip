package ujf.verimag.bip.java.api;

import java.util.ArrayList;
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
