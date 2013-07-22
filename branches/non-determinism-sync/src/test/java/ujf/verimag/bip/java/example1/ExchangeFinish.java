package ujf.verimag.bip.java.example1;


import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

@SuppressWarnings("unchecked")
public class ExchangeFinish extends SyncComponent {

	private Location l0 = new Location(this);
	
	private WrapType<Integer> min = new WrapType<Integer>(this);
	private WrapType<Integer> max = new WrapType<Integer>(this);

	public ReceivePort p1 = new ReceivePort(this);
	public ReceivePort p2 = new ReceivePort(this);
	
	public SendPort work = new SendPort(this, min, max);
	
	public ExchangeFinish(Compound compound) {
		super(compound);
		
		setInitial(l0);
		/**
		 *  Note that g1 and g2 are mutually exclusive, otherwise the engine will not behave properly.  
		 */
		addTransition(new TransitionSyncComponent(l0, l0, null, p1, p2) {
			public boolean guard() {  
				return ((WrapType<Integer>) p1.getVariable(1)).getValue() > ((WrapType<Integer>) p2.getVariable(0)).getValue(); //==> g1
			}
			public void action() {
				int tmpMaxp1 = ((WrapType<Integer>) p1.getVariable(1)).getValue();
				int tmpMinp2 = ((WrapType<Integer>) p2.getVariable(0)).getValue();
				((WrapType<Integer>) p1.getVariable(1)).setValue(tmpMinp2);
				((WrapType<Integer>) p2.getVariable(0)).setValue(tmpMaxp1);
			}

			public void upAction() { } 
			
		});
		
		addTransition(new TransitionSyncComponent(l0, l0, work, p1, p2) {
			public boolean guard() {
				return ((WrapType<Integer>)p1.getVariable(1)).getValue() <= ((WrapType<Integer>) p2.getVariable(0)).getValue(); //==> g2
			}
			public void action() {
				((WrapType<Integer>)p1.getVariable(0)).setValue(min.getValue());
				((WrapType<Integer>)p2.getVariable(1)).setValue(max.getValue());	
			}
			public void upAction() {
				min.setValue( ((WrapType<Integer>)p1.getVariable(0)).getValue() );
				max.setValue( ((WrapType<Integer>)p2.getVariable(1)).getValue() );
			}
		});
		
	}

}
