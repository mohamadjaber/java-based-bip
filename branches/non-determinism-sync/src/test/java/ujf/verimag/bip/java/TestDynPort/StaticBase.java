/**
 * 
 */
package ujf.verimag.bip.java.TestDynPort;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapType;

/**
 * @author bliudze
 *
 */
public class StaticBase extends BaseComponent {

	private Location l0 = new Location(this);
	private Location l1 = new Location(this);
	
	WrapType<SendPort> port = new WrapType<SendPort>(this);
	public SendPort init = new SendPort(this, port) {private String name = "Base.init";};
	public SendPort work = new SendPort(this) {private String name = "Base.work";};
	
	/**
	 * @param compound
	 */
	public StaticBase(Compound compound) {
		super(compound);
		setInitial(l0);

		port.setValue(work);
		
		addTransition(new Transition(l0, l1, init) {
			public void action() {
				System.out.println("Base init");
			}
		});

		addTransition(new Transition(l1, l1, work) {
			public void action() {
				System.out.println("Base work");
			}
		});
	}

}
