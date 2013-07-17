package ujf.verimag.bip.java.example1;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;


public class TestSorting4 extends Compound {
	
	public TestSorting4() {
		// Base Components
		ArrayAtom comp1 = new ArrayAtom(this, 10);
		ArrayAtom comp2 = new ArrayAtom(this, 10);
		ArrayAtom comp3 = new ArrayAtom(this, 10);
		ArrayAtom comp4 = new ArrayAtom(this, 10);

		
		// Sync Components
		ExchangeFinish sync1 = new ExchangeFinish(this);
		ExchangeFinish sync2 = new ExchangeFinish(this);

		Exchange top = new Exchange(this);
		
		// Connections
		sync1.p1.connect(comp1.work);
		sync1.p2.connect(comp2.work);
		
		sync2.p1.connect(comp3.work);
		sync2.p2.connect(comp4.work);
		
		top.p1.connect(sync1.work);
		top.p2.connect(sync2.work);
	}
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting4 top = new TestSorting4();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
	
}
