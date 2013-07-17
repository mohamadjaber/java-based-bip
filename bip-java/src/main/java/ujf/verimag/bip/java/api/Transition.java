package ujf.verimag.bip.java.api;

public abstract class Transition extends AbstractTransition {
	public Transition(Location origin, Location destination, SendPort sendPort) {
		super(origin, destination, sendPort);
	}

}
