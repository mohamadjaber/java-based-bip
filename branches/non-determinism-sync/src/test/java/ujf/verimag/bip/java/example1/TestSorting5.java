package ujf.verimag.bip.java.example1;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;


public class TestSorting5 extends Compound {
	
	public TestSorting5() {
		// Base Components
		ArrayAtom comp1 = new ArrayAtom(this, 10);
		ArrayAtom comp2 = new ArrayAtom(this, 10);
		ArrayAtom comp3 = new ArrayAtom(this, 10);
		ArrayAtom comp4 = new ArrayAtom(this, 10);
		ArrayAtom comp5 = new ArrayAtom(this, 10);
		ArrayAtom comp6 = new ArrayAtom(this, 10);
		ArrayAtom comp7 = new ArrayAtom(this, 10);
		ArrayAtom comp8 = new ArrayAtom(this, 10);

		// Sync Components
		ExchangeFinish sync1 = new ExchangeFinish(this);
		ExchangeFinish sync2 = new ExchangeFinish(this);
		ExchangeFinish sync3 = new ExchangeFinish(this);
		ExchangeFinish sync4 = new ExchangeFinish(this);
		ExchangeFinish sync5 = new ExchangeFinish(this);
		ExchangeFinish sync6 = new ExchangeFinish(this);

		Exchange top = new Exchange(this);
		
		// Connections
		sync1.p1.connect(comp1.work);
		sync1.p2.connect(comp2.work);
		sync2.p1.connect(comp3.work);
		sync2.p2.connect(comp4.work);
		sync3.p1.connect(comp5.work);
		sync3.p2.connect(comp6.work);
		sync4.p1.connect(comp7.work);
		sync4.p2.connect(comp8.work);
		
		sync5.p1.connect(sync1.work);
		sync5.p2.connect(sync2.work);
		sync6.p1.connect(sync3.work);
		sync6.p2.connect(sync4.work);
		
		top.p1.connect(sync5.work);
		top.p2.connect(sync6.work);
	}
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting5 top = new TestSorting5();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
