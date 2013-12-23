/**
 * 
 */
package ujf.verimag.bip.java.TestTwoBases;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

/**
 * @author bliudze
 *
 */
public class TestTwoBases extends Compound {

	/**
	 * 
	 */
	public TestTwoBases() {
		Base base0 = new Base(this, "one");
		Base base1 = new Base(this, "two");
		Sync sync = new Sync(this);
		
		sync.p0.connect(base0.p0);
		sync.p1.connect(base1.p0);
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		TestTwoBases top = new TestTwoBases();
		EngineImpl engine = new EngineImpl(top);
	}

}
