package ujf.verimag.bip.java.dining;


import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;
public class Eat extends SyncComponent {

	private Location l0 = new Location(this);
	
	public ReceivePort p1 = new ReceivePort(this);
	public ReceivePort p2 = new ReceivePort(this);

	
	public Eat(Compound compound, String n) {
		super(compound);
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, p1,p2)	{
			
			@SuppressWarnings("unchecked")
			public boolean guard() {
				System.out.println("Eat: Identity p1: "+ ((WrapType<Integer>) p1.getVariable(0)).getValue() +" Identity p2: "+((WrapType<Integer>) p2.getVariable(0)).getValue());

				return ((WrapType<Integer>) p1.getVariable(0)).getValue() == ((WrapType<Integer>) p2.getVariable(0)).getValue();
//				return true;
			}
			
			@SuppressWarnings("unchecked")
			public void action() {
				System.out.println("Eat: Philosopher with identity "+ ((WrapType<Integer>)p1.getVariable(0)).getValue() + " is chosen.");
				int tmpp1 = ((WrapType<Integer>) p1.getVariable(0)).getValue();
				int tmpp2 = ((WrapType<Integer>) p2.getVariable(0)).getValue();
				((WrapType<Integer>) p1.getVariable(0)).setValue(tmpp2);
				((WrapType<Integer>) p2.getVariable(0)).setValue(tmpp1);
				
			}
			public void upAction() { } 
		});
		

	}

}
