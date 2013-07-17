package ujf.verimag.bip.java.api;

public abstract class AbstractTransition {

	protected Location origin;
	protected Location destination; 
	protected SendPort sendPort;
	
	public AbstractTransition(Location origin, Location destination, SendPort sendPort) {		
		this.origin = origin;
		this.sendPort = sendPort; 
		this.destination = destination; 
		
		this.origin.getOutgoingTransition().add(this);
		this.origin.getIncomingTransition().add(this);
	}
	
	public Location getOrigin() {
		return origin; 
	}
	
	public Location getDestination() {
		return destination; 
	}
	
	public SendPort getSendPort() {
		return sendPort;
	}
	
	/**
	 * Default guard
	 */
	public boolean guard() {
		return true; 
	}
	
	/**
	 * Default action
	 */
	public void action() {
		
	}
}
