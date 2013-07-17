package ujf.verimag.bip.java.example1;


import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapInt;

public class AtomTest extends BaseComponent {
	private WrapInt x = new WrapInt();
	private WrapInt y = new WrapInt();
	
	// Locations
	private Location l0 = new Location();
	private Location l1 = new Location();
	private Location l2 = new Location();

	// Ports
	public SendPort p1 = new SendPort(x, y);
	public SendPort p2 = new SendPort(y);

	
	public AtomTest(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition( new Transition(l0, l1, p1) {
			public boolean guard()  { return x.value > 0; }
			public void action()    { }
		});
		
		addTransition( new Transition(l0, l2, p2) {
			public boolean guard()  { return y.value < 0; }
			public void action()    { }
		});
	}
}
