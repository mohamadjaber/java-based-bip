/**
 * 
 */
package ujf.verimag.bip.java.TestTwoBases;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;

/**
 * @author bliudze
 *
 */
public class Sync extends SyncComponent {

	private Location l0 = new Location(this);
	
	public ReceivePort p0 = new ReceivePort(this);
	public ReceivePort p1 = new ReceivePort(this);
	
	/**
	 * @param compound
	 */
	public Sync(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, p0, p1) {
			public void action() {
				System.out.println("Sync");
			}
		});
	}

}
