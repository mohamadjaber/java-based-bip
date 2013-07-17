package ujf.verimag.bip.java.example1;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestSorting1  extends Compound {
	public TestSorting1() {
	}

	
	@Test
	public void testMain() throws InterruptedException  {
		TestSorting1 top = new TestSorting1();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
