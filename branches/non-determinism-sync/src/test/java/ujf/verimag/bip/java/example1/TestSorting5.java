package ujf.verimag.bip.java.example1;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;


public class TestSorting5 extends Compound {
	
	public TestSorting5() {
	}
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting5 top = new TestSorting5();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
