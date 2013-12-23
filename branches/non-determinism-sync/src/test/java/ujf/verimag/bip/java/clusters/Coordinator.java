/**
 * 
 */
package ujf.verimag.bip.java.clusters;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

/**
 * @author Simon Bliudze (EPFL IC IIF RiSD)
 *
 */
public class Coordinator extends SyncComponent {

	private Location l0 = new Location(this);
		
	public ReceivePort processor = new ReceivePort(this);
	public ReceivePort task = new ReceivePort(this);
	
	/**
	 * @param compound
	 */
	public Coordinator(Compound compound) {
		super(compound);
		setInitial(l0);
		
		addTransition(new TransitionSyncComponent(l0, l0, null, processor, task) {
			public void action(){
				SendPort work = ((WrapType<SendPort>) task.getVariable(0)).getValue();
				((WrapType<SendPort>) processor.getVariable(0)).setValue(work);
				
				Integer taskId = ((WrapType<Integer>) task.getVariable(1)).getValue();
				((WrapType<Integer>) processor.getVariable(1)).setValue(taskId);
				
				Integer procId = ((WrapType<Integer>) processor.getVariable(2)).getValue();
				((WrapType<Integer>) task.getVariable(2)).setValue(procId);
	
			}
		});
	}

}
