package ujf.verimag.bip.java.modulo;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;

public class Modulo2Sync extends SyncComponent {

	private Location l0 = new Location();
	private Location l1 = new Location(); 
	
	public SendPort s = new SendPort();
	public ReceivePort r = new ReceivePort();
	
	private String name; 
	
	public Modulo2Sync(Compound compound, String n) {
		super(compound);
		setInitial(l0);
		
		name = n; 
		
		addTransition(new TransitionSyncComponent(l0, l1, null, r)	{
			public void action() {
				System.out.print("(" + name + ","+ 1 + " -- ");
			}
		});
		
		addTransition(new TransitionSyncComponent(l1, l0, s, r)	{
			public void action() {
				System.out.print("(" + name + ","+ 0 + " -- ");
			}
		});

	}

}
