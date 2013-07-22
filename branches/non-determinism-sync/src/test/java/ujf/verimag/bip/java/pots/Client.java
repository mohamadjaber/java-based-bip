package ujf.verimag.bip.java.pots;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapType;

public class Client extends BaseComponent {
	
	private WrapType<Integer> identifier = new WrapType<Integer>(this);
	private WrapType<Integer> calleeIdentifier = new WrapType<Integer>(this);
	private WrapType<Integer> callerIdentifier = new WrapType<Integer>(this);

	
	private WrapType<Integer> from = new WrapType<Integer>(this);
	private WrapType<Integer> to = new WrapType<Integer>(this);
	
	private boolean isCaller;  

	
	private Location l0 = new Location(this);
	private Location l1 = new Location(this);
	private Location l2 = new Location(this);
	
	public SendPort wait = new SendPort(this, callerIdentifier);
	public SendPort dial = new SendPort(this, identifier, calleeIdentifier);
	public SendPort voice = new SendPort(this);
	public SendPort disconnect = new SendPort(this, from, to);

	
	public Client(Compound compound, int id) {
		super(compound);
		setInitial(l0);
		identifier.setValue(id); 
		calleeIdentifier.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
		
		
		addTransition(new Transition(l0, l1, dial) {
			public void action() {
				System.out.println("Client " + identifier + " calls " + calleeIdentifier);
				isCaller = true; 
			}
		});
		
		addTransition(new Transition(l1, l2, voice) {
			public void action() {
				System.out.println("Client " + identifier + " start talking with " + calleeIdentifier);
			}
		});
		
		addTransition(new Transition(l2, l0, disconnect) {
			public void action() {
				System.out.println("Client " + identifier + " closes with " + calleeIdentifier);
				from.setValue(identifier.getValue());
				if(isCaller) {
					from.setValue(identifier.getValue());
					to.setValue(calleeIdentifier.getValue());
				}
				else {
					from.setValue(callerIdentifier.getValue());
					to.setValue(identifier.getValue());
				}
				calleeIdentifier.setValue(Configuration.randomExcept(identifier.getValue(), Configuration.nbOfClients));
			}
		});
		
		addTransition(new Transition(l0, l1, wait) {
			public void action() {
				System.out.println("Client " + identifier + " receives a call from " + calleeIdentifier);
				isCaller = false; 
			}
		});
		
	}


}
