/**
 * 
 */
package ujf.verimag.bip.java.clusters;

import java.util.ArrayList;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;

/**
 * A dummy component with one state and the requested 
 * number of receive ports.  Self-loop transitions on 
 * all receive ports are always enabled and do nothing.
 *
 * @author Simon Bliudze (EPFL IC IIF RiSD)
 */
public class Dummy extends SyncComponent {

	private Location l0 = new Location(this);
	
	public ArrayList<ReceivePort> ports = new ArrayList<ReceivePort>();
	
	/**
	 * Create the dummy component.
	 * 
	 * @param compound	&mdash; the holder Compound object
	 * @param nbPorts	&mdash; the number of dummy receiving ports to create
	 */
	public Dummy(Compound compound, int nbPorts) {
		super(compound);
		
		setInitial(l0);
		
		for (int i = 0; i < nbPorts; i++){
			ports.add(new ReceivePort(this));
			addTransition(new TransitionSyncComponent(l0, l0, null, ports.get(i)) {});
		}		
	}

}
