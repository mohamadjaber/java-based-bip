package ujf.verimag.bip.java.post.simple;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapType;

public class Client extends BaseComponent {
	
	private WrapType<Integer> identifier = new WrapType<Integer>(this);
	
	private WrapType<Integer> from = new WrapType<Integer>(this);
	private WrapType<Integer> to = new WrapType<Integer>(this);
	
	
	private Location l0 = new Location(this);

	
	public SendPort wait = new SendPort(this, identifier, from);
	public SendPort dial = new SendPort(this, identifier, to);

	
	public Client(Compound compound, int id) {
		super(compound);
		setInitial(l0);
		identifier.setValue(id); 
		to.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
		
		addTransition(new Transition(l0, l0, dial) {
			public void action() {
				System.out.println("Client " + identifier.getValue() + " calls " + to.getValue());
				to.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
			}
		});
		
		addTransition(new Transition(l0, l0, wait) {
			public void action() {
				System.out.println("Client " + identifier.getValue() + " receives a call from " + from.getValue());
				to.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
				from.setValue(null);

			}
		});
	}


}
