package ujf.verimag.bip.java.example1;

import static org.junit.Assert.*;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestSorting2 extends Compound {
	public TestSorting2() {
		
	}
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting2 top = new TestSorting2();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
