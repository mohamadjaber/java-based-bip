package ujf.verimag.bip.java.pots;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

public class CalleeAgregation extends SyncComponent {
	private WrapType<Integer> callerId = new WrapType<Integer>(this);
	private WrapType<Integer> calleeId = new WrapType<Integer>(this);
	
	public ReceivePort[] callees = new ReceivePort[Configuration.nbOfClients];
	public SendPort callee = new SendPort(this, calleeId, callerId);
	
	private Location l0 = new Location(this);

	
	public CalleeAgregation(Compound compound) {
		super(compound);
		setInitial(l0);
		
		for(int i = 0; i < Configuration.nbOfClients; i++) {
			callees[i] = new ReceivePort(this);
			final ReceivePort calleeI = callees[i];
			
			addTransition(new TransitionSyncComponent(l0,l0, callee, callees[i]) {
				public void upAction() {
					calleeId.setValue((Integer) calleeI.getVariable(0).getValue());
				}
				
				@SuppressWarnings("unchecked")
				public void action() {
					((WrapType<Integer>)calleeI.getVariable(1)).setValue(callerId.getValue());
				}
			});
		}
	}

}
