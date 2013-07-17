package ujf.verimag.bip.java.example1;

import static org.junit.Assert.*;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestSorting2 extends Compound {
	public TestSorting2() {
		// Base Components
		ArrayAtom comp1 = new ArrayAtom(this, 100);
		ArrayAtom comp2 = new ArrayAtom(this, 100);
		ArrayAtom comp3 = new ArrayAtom(this, 100);
		ArrayAtom comp4 = new ArrayAtom(this, 100);

		
		// Sync Components
		Exchange exchange1 = new Exchange(this);
		Exchange exchange2 = new Exchange(this);

		Finish finish1 = new Finish(this);
		Finish finish2 = new Finish(this);
		
		Exchange top = new Exchange(this);
		
		// Connections
		exchange1.p1.connect(comp1.work);
		exchange1.p2.connect(comp2.work);
		
		exchange2.p1.connect(comp3.work);
		exchange2.p2.connect(comp4.work);
		
		finish1.p1.connect(comp1.work);
		finish1.p2.connect(comp2.work);
		
		finish2.p1.connect(comp3.work);
		finish2.p2.connect(comp4.work);
		
		top.p1.connect(finish1.work);
		top.p2.connect(finish2.work);
	}
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting2 top = new TestSorting2();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}