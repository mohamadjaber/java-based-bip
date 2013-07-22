package ujf.verimag.bip.java.pots;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;


public class CallerCallee extends SyncComponent {

	
	private Location l0 = new Location(this);

	public ReceivePort caller = new ReceivePort(this);
	public ReceivePort callee = new ReceivePort(this);
	
	public CallerCallee(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, caller, callee) {
			public boolean guard() {
				return caller.getVariable(1).getValue() == callee.getVariable(1).getValue();
			}
			
			@SuppressWarnings("unchecked")
			public void action() {
				((WrapType<Integer>)callee.getVariable(0)).setValue(((WrapType<Integer>)caller.getVariable(0)).getValue());
			}
		});
		
	}

}
