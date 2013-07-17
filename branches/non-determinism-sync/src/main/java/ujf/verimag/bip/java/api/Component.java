package ujf.verimag.bip.java.api;

import java.util.HashSet;
import java.util.Set;

public abstract class Component {
	protected Compound compound;
	
	protected Set<AbstractTransition> transitions;
	protected Set<Location> locations;
	protected Set<SendPort> sendPorts;

	
	protected Location initialLocation;
	
	protected Location currentLocation; 
	
	public Component(Compound compound) {
		transitions = new HashSet<AbstractTransition>();
		locations = new HashSet<Location>();
		sendPorts = new HashSet<SendPort>();
		this.compound = compound;
	}
	

	public Set<AbstractTransition> getTransition() {
		return transitions; 
	}
	
	public Set<Location> getLocation() {
		return locations; 
	}
	
	public Location getInitialLocation() {
		return initialLocation;
	}
	
	public void addTransition(AbstractTransition trans) {
		transitions.add(trans);
	}
	
	
	public void addLocation(Location loc) {
		locations.add(loc);
	}
	
	public void addSendPort(SendPort p) {
		sendPorts.add(p);
	}
	
	public Set<SendPort> getSendPort() {
		return sendPorts;
	}
	
	public void setInitial(Location loc) {
		initialLocation = loc;
		currentLocation = loc; 
	}	
	
	public Compound getCompound() {
		return compound;
	}
	
	public Location getCurrentLocation() {
		return currentLocation; 
	}
	
	public void setCurrentLocation(Location l) {
		currentLocation = l; 
	}
	
	
	
	
}
