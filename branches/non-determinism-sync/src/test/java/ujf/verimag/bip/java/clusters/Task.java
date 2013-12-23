/**
 * 
 */
package ujf.verimag.bip.java.clusters;

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
public class Task extends BaseComponent {
	private final int runningBound = 2;
	private final int migrationBound = 5;
	
	private int timer = 0;
	
	// Data that may be attached to ports
	private WrapType<Integer> processor = new WrapType<Integer>(this);
	private WrapType<Integer> taskId = new WrapType<Integer>(this);
	private WrapType<SendPort> portWork = new WrapType<SendPort>(this);
	
	// Locations
	private Location init = new Location(this);
	private Location running = new Location(this);
	private Location paused = new Location(this);
	private Location ready = new Location(this);
	
	// Ports
	public SendPort migrate = new SendPort(this, portWork, taskId, processor);
	public SendPort suspend = new SendPort(this);
	public SendPort resume = new SendPort(this);
	public SendPort tick = new SendPort(this);
	public SendPort timeout = new SendPort(this);
	public SendPort work = new SendPort(this);
	public SendPort dummy = new SendPort(this);
	
	public Task(Compound compound, int id) {
		super(compound);
		taskId.setOriginalValue(id);
		processor.setOriginalValue(-1);
		portWork.setOriginalValue(work);
		
		setInitial(init);
		
		addTransition(new Transition(init, running, migrate) {
			public void action() {
				System.out.println("Task " + taskId.getValue() + ": migrating...");
			}
		});
		
		addTransition(new Transition(running, running, work) {
			public void action(){
				System.out.println("Task " + taskId.getValue() + " on Processor " + processor.getValue() + ": working...");
			}
		});
		
		addTransition(new Transition(running, paused, suspend){
			public boolean guard() {
				return false;
			}
			public void action(){
				timer = 0;
				System.out.println("Task " + taskId.getValue() + ": running --suspend--> paused");
			}
		});
		
		addTransition(new Transition(paused, paused, tick){
			public void action(){
				timer++;
				System.out.println("Task " + taskId.getValue() + ": tick " + timer);
			}
		});
		
		addTransition(new Transition(paused, running, resume){
			public boolean guard(){
				return timer > runningBound && timer < migrationBound;  
			}
			public void action(){
				System.out.println("Task " + taskId.getValue() + ": paused --resume--> running");
			}
		});
		
		addTransition(new Transition(paused, ready, timeout){
			public boolean guard(){
				return timer >= migrationBound;
			}
			public void action(){
				System.out.println("Task " + taskId.getValue() + ": paused --timeout--> ready");
			}
		});
		
		addTransition(new Transition(ready, running, dummy){
			public void action(){
				System.out.println("Task " + taskId.getValue() + ": migrate");
			}
		});
	}

}
