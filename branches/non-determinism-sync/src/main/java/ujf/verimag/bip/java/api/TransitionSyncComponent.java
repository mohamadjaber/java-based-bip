package ujf.verimag.bip.java.api;



public abstract class TransitionSyncComponent extends AbstractTransition {
	
	private ReceivePort[] receivePorts;
	
	public TransitionSyncComponent(Location origin, Location destination, SendPort sendPort, ReceivePort...ports) {
		super(origin, destination, sendPort);
		receivePorts = ports;
	}
	
	public void upAction() { }
	
	public ReceivePort[] getReceivePorts() {
		return receivePorts;
	}

}
