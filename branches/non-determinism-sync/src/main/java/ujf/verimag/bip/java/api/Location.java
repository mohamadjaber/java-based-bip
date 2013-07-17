package ujf.verimag.bip.java.api;

import java.util.LinkedList;
import java.util.List;

public class Location {
	
	private Component component; 
	
	private List<AbstractTransition> outgoingTransitions;
	private List<AbstractTransition> incomingTransitions;

	
	public Location() {
		outgoingTransitions = new LinkedList<AbstractTransition>();
		incomingTransitions = new LinkedList<AbstractTransition>();
	}
	
	public List<AbstractTransition> getOutgoingTransition() {
		return outgoingTransitions;
	}
	
	public List<AbstractTransition> getIncomingTransition() {
		return incomingTransitions;
	}
	
	public void setComponent(Component component) {
		this.component = component;
		this.getComponent().addLocation(this);
	}
	
	public Component getComponent() {
		return component; 
	}
	
}
