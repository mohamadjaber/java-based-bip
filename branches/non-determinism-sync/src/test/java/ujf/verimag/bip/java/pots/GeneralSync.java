package ujf.verimag.bip.java.pots;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;

public class GeneralSync extends SyncComponent {

	private Location l0 = new Location(this);

	public ReceivePort rcv1 = new ReceivePort(this);
	public ReceivePort rcv2 = new ReceivePort(this);
	
	
	public GeneralSync(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, rcv1, rcv2) {
			public boolean guard() {
				return rcv1.getVariable(0).getValue() == rcv2.getVariable(0).getValue() &&
						rcv1.getVariable(1).getValue() == rcv2.getVariable(1).getValue();
			}
		});
	}

}
