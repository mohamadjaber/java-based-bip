package ujf.verimag.bip.java.example1;


import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

@SuppressWarnings("unchecked")
public class Finish extends SyncComponent {
	
	private WrapType<Integer> min = new WrapType<Integer>(this);
	private WrapType<Integer> max = new WrapType<Integer>(this);

	public ReceivePort p1 = new ReceivePort(this);
	public ReceivePort p2 = new ReceivePort(this);
	
	public SendPort work = new SendPort(this, min, max);
	
	private Location l0 = new Location(this);
	
	public Finish(Compound compound) {
		super(compound);
		setInitial(l0);

		addTransition(new TransitionSyncComponent(l0, l0, work, p1, p2) {
			public boolean guard() {
				return ((WrapType<Integer>)p1.getVariable(1)).getValue() <= ((WrapType<Integer>) p2.getVariable(0)).getValue();
			}
			public void action() {
				((WrapType<Integer>)p1.getVariable(0)).setValue( min.getValue());
				((WrapType<Integer>)p2.getVariable(1)).setValue( max.getValue());	
			}
			public void upAction() {
				min.setValue( ((WrapType<Integer>)p1.getVariable(0)).getValue() );
				max.setValue( ((WrapType<Integer>)p2.getVariable(1)).getValue() );
			}
		});
	}


}
