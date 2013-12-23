/**
 * 
 */
package ujf.verimag.bip.java.clusters;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;

/**
 * @author bliudze
 *
 */
public class DummyBase extends BaseComponent {

	private Location l0 = new Location(this);
	
	public SendPort dummy = new SendPort(this);
	
	/**
	 * @param compound
	 */
	public DummyBase(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new Transition(l0, l0, dummy) {
			public void action() {
//				System.out.println("Dummy");
			}
		});
	}

}
