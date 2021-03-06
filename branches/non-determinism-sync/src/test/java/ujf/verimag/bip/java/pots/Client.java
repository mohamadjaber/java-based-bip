package ujf.verimag.bip.java.pots;

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
	
	private boolean isCalling = true; 
	
	private Location l0 = new Location(this);
	private Location l1 = new Location(this);
	private Location l2 = new Location(this);
	
	public SendPort wait = new SendPort(this, identifier, from);
	public SendPort dial = new SendPort(this, identifier, to);
	public SendPort voice = new SendPort(this, from, to);
	public SendPort disconnect = new SendPort(this, from, to);

	
	public Client(Compound compound, int id) {
		super(compound);
		setInitial(l0);
		identifier.setValue(id); 
		to.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
		
		addTransition(new Transition(l0, l1, dial) {
			public void action() {
				System.out.println("Client " + identifier.getValue() + " calls " + to.getValue());
				isCalling = true; 
				from.setValue(identifier.getValue());
			}
		});
		
		addTransition(new Transition(l0, l1, wait) {
			public void action() {
				System.out.println("Client " + identifier.getValue() + " receives a call from " + from.getValue());
				isCalling = false; 
				to.setValue(identifier.getValue());
			}
		});
		
		addTransition(new Transition(l1, l2, voice) {
			public void action() {
				if(isCalling)
					System.out.println("Client " + identifier.getValue() + " start talking with " + to.getValue());
				else
					System.out.println("Client " + identifier.getValue() + " start talking with " + from.getValue());
			}
		});
		
		addTransition(new Transition(l2, l0, disconnect) {
			public void action() {
				if(isCalling) 
					System.out.println("Client " + identifier.getValue() + " closes with " + to.getValue());
				else
					System.out.println("Client " + identifier.getValue() + " closes with " + from.getValue());
				
				to.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
			}
		});
	}


}
