/**
 * 
 */
package ujf.verimag.bip.java.clusters;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;

/**
 * @author bliudze
 *
 */
public class Dummy2 extends SyncComponent {

	public ReceivePort p1 = new ReceivePort(this);
	public ReceivePort p2 = new ReceivePort(this);
	
	private Location l0 = new Location(this);
	
	/**
	 * @param compound
	 */
	public Dummy2(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, p1) {});
		addTransition(new TransitionSyncComponent(l0, l0, null, p2) {});
	}

}
