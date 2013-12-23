/**
 * 
 */
package ujf.verimag.bip.java.clusters;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

/**
 * @author Simon Bliudze (EPFL IC IIF RiSD)
 * 
 */
public class TestTask extends Compound {
	private final int memory = 1;
	private final int tasks = memory;
	
	/**
	 * 
	 */
	public TestTask() {
		Task task = new Task(this, 0);
		Processor proc = new Processor(this, 0, memory, tasks);
		
		Coordinator coordinator = new Coordinator(this);
		coordinator.task.connect(task.migrate);
		coordinator.processor.connect(proc.accept);

		DummyBase dummyAcc = new DummyBase(this);
		proc.dummyAcc.connect(dummyAcc.dummy);
		
		DummyBase dummyUp = new DummyBase(this);
		proc.dummyUp.connect(dummyUp.dummy);
		
		DummyBase dummyDown = new DummyBase(this);
		proc.dummyDown.connect(dummyDown.dummy);
		
		Dummy dummyTop = new Dummy(this, 7);

		dummyTop.ports.get(0).connect(task.dummy);
		dummyTop.ports.get(1).connect(task.suspend);
		dummyTop.ports.get(2).connect(task.resume);
		dummyTop.ports.get(3).connect(task.tick);
		dummyTop.ports.get(4).connect(task.timeout);
		
		dummyTop.ports.get(5).connect(proc.memDown);
		dummyTop.ports.get(6).connect(proc.memUp);		
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		TestTask top = new TestTask();
		EngineImpl engine = new EngineImpl(top);
	}
}
