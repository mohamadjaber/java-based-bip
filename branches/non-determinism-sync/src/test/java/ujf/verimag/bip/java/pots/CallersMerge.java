package ujf.verimag.bip.java.pots;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

public class CallersMerge extends SyncComponent {
	
	private WrapType<Integer> from = new WrapType<Integer>(this);
	private WrapType<Integer> to = new WrapType<Integer>(this);
	
	public ReceivePort[] callers = new ReceivePort[Configuration.nbOfClients];
	public SendPort caller = new SendPort(this, from, to);
	
	private Location l0 = new Location(this);

	
	public CallersMerge(Compound compound) {
		super(compound);
		setInitial(l0);
		
		for(int i = 0; i < Configuration.nbOfClients; i++) {
			callers[i] = new ReceivePort(this);
			final ReceivePort dialler = callers[i];
			
			addTransition(new TransitionSyncComponent(l0, l0, caller, callers[i]) {
				public void upAction() {
					from.setValue((Integer) dialler.getVariable(0).getValue());
					to.setValue((Integer) dialler.getVariable(0).getValue());
				}
				
				@SuppressWarnings("unchecked")
				public void action() {
					((WrapType<Integer>) dialler.getVariable(0)).setValue(from.getValue());
					((WrapType<Integer>) dialler.getVariable(1)).setValue(to.getValue());

				}
			});
		}
		
		
		
	}

}
