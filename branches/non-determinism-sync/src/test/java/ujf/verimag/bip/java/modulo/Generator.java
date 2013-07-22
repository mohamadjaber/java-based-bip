package ujf.verimag.bip.java.modulo;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;

public class Generator extends BaseComponent {

	private Location l0 = new Location(this);

	public SendPort s = new SendPort(this);
	
	public Generator(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new Transition(l0,l0,s) {	
			public void action() {
				System.out.println();
			}
		});
	}

}
