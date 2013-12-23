package ujf.verimag.bip.java.dining;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapType;

public class Philosopher extends BaseComponent {

	private Location l0 = new Location(this);
	private Location l1 = new Location(this);
	private WrapType<Integer> identity = new WrapType<Integer>(this);
	public SendPort get = new SendPort(this, identity);

	
	public Philosopher(Compound compound, final int id) {
		super(compound);
		setInitial(l0);
		identity.setValue(id);
		
		addTransition(new Transition(l0,l1,get) {	
			public void action() {
				System.out.println("Philosopher: with identity "+ id +" picks forks. ");
			}
		});
	}

}
