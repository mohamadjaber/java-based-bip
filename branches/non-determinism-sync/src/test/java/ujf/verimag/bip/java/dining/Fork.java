package ujf.verimag.bip.java.dining;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

public class Fork extends SyncComponent {

	private Location l0 = new Location(this);
	private WrapType<Integer> identity = new WrapType<Integer>(this);
	
	public SendPort s = new SendPort(this, identity);
	public ReceivePort rp1 = new ReceivePort(this);
	public ReceivePort rp2 = new ReceivePort(this);

	public Fork(Compound compound, final String n) {
		super(compound);
		setInitial(l0);
		
	
		addTransition(new TransitionSyncComponent(l0, l0, s, rp1){
			@SuppressWarnings("unchecked")
			public void action() {
				((WrapType<Integer>)rp1.getVariable(0)).setValue(identity.getValue());
			}
			
			@SuppressWarnings("unchecked")
			public void upAction() { 
				identity.setValue( ((WrapType<Integer>)rp1.getVariable(0)).getValue());
				System.out.println("Fork: UpAction in "+ n + " rp1 with identity: "+ identity.getValue());
			} 
		}
		);
		
		addTransition(new TransitionSyncComponent(l0, l0, s, rp2)	{
			@SuppressWarnings("unchecked")
			public void action() {
				((WrapType<Integer> )rp2.getVariable(0)).setValue(identity.getValue());
			}
			
			@SuppressWarnings("unchecked")
			public void upAction() { 
				identity.setValue(((WrapType<Integer> )rp2.getVariable(0)).getValue());
				System.out.println("Fork: UpAction in "+ n + " rp2 with identity: "+ identity.getValue());
			} 
		});
		

	}

}
