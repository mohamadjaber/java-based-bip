package ujf.verimag.bip.java.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Compound {
	private Set<BaseComponent> baseComponents;
	private Set<SyncComponent> syncComponents;

	private Map<ReceivePort, SendPort> connections;
	
	public Compound() {	
		baseComponents = new HashSet<BaseComponent>();
		syncComponents = new HashSet<SyncComponent>();
		
		connections = new HashMap<ReceivePort, SendPort>();
	}
	
	
	public Set<BaseComponent> getBaseComponents() {
		return baseComponents;
	}
	
	public Set<SyncComponent> getSyncComponents() {
		return syncComponents;
	}
	
	public Map<ReceivePort, SendPort> getConnections() {
		return connections;
	}
	
	public void resetSyncComponents() {
		for(SyncComponent syncComponent: syncComponents) {
			syncComponent.reset();
		}
	}

}
