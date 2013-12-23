/**
 * 
 */
package ujf.verimag.bip.java.clusters;

import ujf.verimag.bip.java.api.ReceivePort;
import ujf.verimag.bip.java.api.SyncComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.TransitionSyncComponent;
import ujf.verimag.bip.java.types.WrapType;

/**
 * @author bliudze
 *
 */
public class Processor extends SyncComponent {	
	private final int tasksMax;
	private final int memMax;
	private int tasksCurrent = 0;
	private int memCurrent;
		
	private Location l0 = new Location(this);
	
	public SendPort memUp = new SendPort(this) {private String name = "Processor.memUp";};
	public ReceivePort dummyUp = new ReceivePort(this) {private String name = "Processor.dummyUp";};
	public SendPort memDown = new SendPort(this) {private String name = "Processor.memDown";};
	public ReceivePort dummyDown = new ReceivePort(this) {private String name = "Processor.dummyDown";};

	private WrapType<Integer> procId = new WrapType<Integer>(this);
	private WrapType<Integer> taskId = new WrapType<Integer>(this);
	private WrapType<SendPort> port = new WrapType<SendPort> (this);
	public SendPort accept = new SendPort(this, port, taskId, procId) {private String name = "Processor.accept";};
	public ReceivePort dummyAcc = new ReceivePort(this) {private String name = "Processor.dummyAcc";};

	/**
	 * @param compound
	 */
	public Processor(Compound compound, int id, int memory, int tasks) {
		
		super(compound);
		tasksMax = tasks;
		memMax = memory;
		memCurrent = memory;
		
		procId.setOriginalValue(id);
		taskId.setOriginalValue(-1);
		port.setOriginalValue(null);
		setInitial(l0);
	
		/*
		 * This transition sets up a connection with a new task.
		 * 
		 * It is only possible if the number of tasks is below the allowed maximum.
		 * 
		 * For each new task, a new run port is created and connected to 
		 * the corresponding "work" port passed as a data value. 
		 * 
		 * A corresponding transition is set up, allowed
		 * only when the current number of (running) tasks is lower than
		 * the current available memory. (It is assumed that each task uses
		 * one unit of memory.)
		 * 
		 */
		addTransition(new TransitionSyncComponent(l0, l0, accept, dummyAcc) {				
			public boolean guard(){
				return tasksCurrent < tasksMax;
			}
			public void action(){
				System.out.println("Processor " + procId.getValue() + ": accepting Task " + taskId.getValue());
				taskId.setValue(-1);
				final ReceivePort run = new ReceivePort(Processor.this) {
					private String name = "Processor.run" + tasksCurrent;
				}; 

				addTransition(new TransitionSyncComponent(l0, l0, null, run) {
					public boolean guard() {
						return tasksCurrent <= memCurrent; 
					}
					public void action() {						
						System.out.println("Processor " + procId.getValue() + ": running");
						
					}
				});	
				run.connect(port.getValue());
				port.setValue(null);
			}
		});
		
		addTransition(new TransitionSyncComponent(l0, l0, memUp, dummyUp) {
			public boolean guard() {
				return false; //memCurrent < memMax;
			}
			public void action() {
				memCurrent++;
				System.out.println("Processor " + procId.getValue() + ": available memory = " + memCurrent);
			}
		});
		
		addTransition(new TransitionSyncComponent(l0, l0, memDown, dummyDown) {
			public boolean guard(){
				return false; //memCurrent > 0;
			}
			public void action(){
				memCurrent--;
				System.out.println("Processor " + procId.getValue() + ": available memory = " + memCurrent);
			}
		});
	}

}
