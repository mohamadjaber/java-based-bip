package ujf.verimag.bip.java.api;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class Component {
	protected Compound compound;
	
	protected Set<AbstractTransition> transitions;
	protected Set<Location> locations;
	protected Set<SendPort> sendPorts;

	
	protected Location initialLocation;
	
	protected Location currentLocation; 
	
	protected List<TransitionEnabled> currentTransitionsEnabled;
	protected List<TransitionEnabled> allTransitionsEnabled;
	
	private int indexTransitionEnabled;

	
	public Component(Compound compound) {
		transitions = new HashSet<AbstractTransition>();
		locations = new HashSet<Location>();
		sendPorts = new HashSet<SendPort>();
		this.compound = compound;
		
		currentTransitionsEnabled = new LinkedList<TransitionEnabled>();
		allTransitionsEnabled = new LinkedList<TransitionEnabled>();
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
	
	public int getCurrentIndexNotified() {
		return allTransitionsEnabled.size();
	}
	
	public void setIndexTransitionEnabled(int index) {
		indexTransitionEnabled = index; 
	}
	
	public int getIndexTransitionEnabled() {
		return indexTransitionEnabled;
	}
	
	public TransitionEnabled getTransitionEnabled(int index) {
		return currentTransitionsEnabled.get(index);
	}
}
