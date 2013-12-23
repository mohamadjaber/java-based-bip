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
public class DynamicSync extends SyncComponent {

	private Location l0 = new Location(this);
	private Location l1 = new Location(this);
	
	WrapType<SendPort> port = new WrapType<SendPort>(this);
	public SendPort init = new SendPort(this, port) {
		private String name = "Sync.init";
	};
	
	public ReceivePort dummy = new ReceivePort(this) {
		private String name = "Sync.dummy";
	}; 
	
	/**
	 * @param compound
	 */
	public DynamicSync(Compound compound) {
		super(compound);
		setInitial(l0);

		addTransition(new TransitionSyncComponent(l0, l1, init, dummy) { 
			public void action() {
				ReceivePort work = new ReceivePort(DynamicSync.this) {
					private String name = "Sync.work";
				};

				addTransition(new TransitionSyncComponent(l1, l1, null, work) {
					public void action() {
						System.out.println("Sync work");
					}
				});

				work.connect(port.getValue());
				System.out.println("Sync init");
			}
		});
		
	}

}
