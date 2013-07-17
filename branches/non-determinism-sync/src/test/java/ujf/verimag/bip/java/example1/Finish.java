package ujf.verimag.bip.java.example1;


import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapInt;

public class Finish extends SyncComponent {
	
	private WrapInt min = new WrapInt();
	private WrapInt max = new WrapInt();

	public ReceivePort p1 = new ReceivePort();
	public ReceivePort p2 = new ReceivePort();
	
	public SendPort work = new SendPort(min, max);
	
	private Location l0 = new Location();
	
	public Finish(Compound compound) {
		super(compound);
		setInitial(l0);

		addTransition(new TransitionSyncComponent(l0, l0, work, p1, p2) {
			public boolean guard() {
				return ((WrapInt)p1.getVariable(1)).value <= ((WrapInt) p2.getVariable(0)).value;
			}
			public void action() {
				((WrapInt)p1.getVariable(0)).value = min.value;
				((WrapInt)p2.getVariable(1)).value = max.value;	
			}
			public void upAction() {
				min.value = ((WrapInt)p1.getVariable(0)).value;
				max.value = ((WrapInt)p2.getVariable(1)).value;
			}
		});
	}


}
