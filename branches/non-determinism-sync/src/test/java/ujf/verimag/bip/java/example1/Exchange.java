package ujf.verimag.bip.java.example1;


import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapInt;


public class Exchange extends SyncComponent {
	
	public ReceivePort p1 = new ReceivePort();
	public ReceivePort p2 = new ReceivePort();

	private Location l0 = new Location();
	
	public Exchange(Compound compound) {
		super(compound);
		
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, p1, p2) {
			public boolean guard() {  
				return ((WrapInt) p1.getVariable(1)).value > ((WrapInt) p2.getVariable(0)).value;
			}
			public void action() {
				int tmpMaxp1 = ((WrapInt) p1.getVariable(1)).value;
				int tmpMinp2 = ((WrapInt) p2.getVariable(0)).value;
				((WrapInt) p1.getVariable(1)).value = tmpMinp2;
				((WrapInt) p2.getVariable(0)).value = tmpMaxp1;
			}

			public void upAction() { } 
			
		});
		
	}

}
