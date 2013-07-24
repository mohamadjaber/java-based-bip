package ujf.verimag.bip.java.pots;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

public class GeneralAgregation extends SyncComponent {
	
	private WrapType<Integer> from = new WrapType<Integer>(this);
	private WrapType<Integer> to = new WrapType<Integer>(this);
	
	public ReceivePort[] receivers = new ReceivePort[Configuration.nbOfClients];
	public SendPort sender = new SendPort(this, from, to);
	
	private Location l0 = new Location(this);

	
	public GeneralAgregation(Compound compound) {
		super(compound);
		setInitial(l0);
		
		for(int i = 0; i < Configuration.nbOfClients; i++) {
			receivers[i] = new ReceivePort(this);
			final ReceivePort receiverI = receivers[i];
			
			addTransition(new TransitionSyncComponent(l0, l0, sender, receivers[i]) {
				public void upAction() {
					from.setValue((Integer) receiverI.getVariable(0).getValue());
					to.setValue((Integer) receiverI.getVariable(1).getValue());
				}
			});
		}
		
		
		
	}

}
