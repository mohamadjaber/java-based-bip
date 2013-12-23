/**
 * 
 */
package ujf.verimag.bip.java.TestDynPort;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

/**
 * @author bliudze
 *
 */
public class TestDynPort extends Compound {

	/**
	 * 
	 */
	public TestDynPort() {
		StaticBase base = new StaticBase(this);
		DynamicSync sync = new DynamicSync(this);
		Coordinator coord = new Coordinator(this);
		DummyBase dummy = new DummyBase(this);
		
		coord.base.connect(base.init);
		coord.sync.connect(sync.init);
		
		sync.dummy.connect(dummy.dummy);
//		sync.work.connect(base.work);
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		TestDynPort top = new TestDynPort();
		EngineImpl engine = new EngineImpl(top);
	}
}
