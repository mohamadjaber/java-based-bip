/**
 * 
 */
package ujf.verimag.bip.java.TestDynPort;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

/**
 * @author bliudze
 *
 */

public class Coordinator extends SyncComponent {

	private Location l0 = new Location(this);
	private Location l1 = new Location(this);
	
	public ReceivePort base = new ReceivePort(this) {
		private String name = "Coordinator.base";
	};
	
	public ReceivePort sync = new ReceivePort(this) {
		private String name = "Coordinator.sync";
	};
	/**
	 * @param compound
	 */
	public Coordinator(Compound compound) {
		super(compound);
		setInitial(l0);

		addTransition(new TransitionSyncComponent(l0, l1, null, base, sync) {
			public void action() {
				SendPort port = ((WrapType<SendPort>) base.getVariable(0)).getValue();
				((WrapType<SendPort>) sync.getVariable(0)).setValue(port);
				System.out.println("Coordinator");
			}
		});
	}

}
