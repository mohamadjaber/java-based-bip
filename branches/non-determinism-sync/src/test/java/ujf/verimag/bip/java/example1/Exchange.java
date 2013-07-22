package ujf.verimag.bip.java.example1;


import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

@SuppressWarnings("unchecked")
public class Exchange extends SyncComponent {
	
	public ReceivePort p1 = new ReceivePort(this);
	public ReceivePort p2 = new ReceivePort(this);

	private Location l0 = new Location(this);
	
	public Exchange(Compound compound) {
		super(compound);
		
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, p1, p2) {
			public boolean guard() {  
				return ((WrapType<Integer>) p1.getVariable(1)).getValue() > ((WrapType<Integer>) p2.getVariable(0)).getValue();
			}
			public void action() {
				int tmpMaxp1 = ((WrapType<Integer>) p1.getVariable(1)).getValue();
				int tmpMinp2 = ((WrapType<Integer>) p2.getVariable(0)).getValue();
				((WrapType<Integer>) p1.getVariable(1)).setValue(tmpMinp2);
				((WrapType<Integer>) p2.getVariable(0)).setValue(tmpMaxp1);
			}

			public void upAction() { } 
			
		});
		
	}

}
