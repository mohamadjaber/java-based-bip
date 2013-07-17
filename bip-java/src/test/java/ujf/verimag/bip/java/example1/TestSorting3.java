package ujf.verimag.bip.java.example1;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;


public class TestSorting3 extends Compound {
	
	public TestSorting3() {
		// Base Components
		ArrayAtom comp1 = new ArrayAtom(this, 5);
		ArrayAtom comp2 = new ArrayAtom(this, 5);
		ArrayAtom comp3 = new ArrayAtom(this, 5);
		ArrayAtom comp4 = new ArrayAtom(this, 5);
		ArrayAtom comp5 = new ArrayAtom(this, 5);
		ArrayAtom comp6 = new ArrayAtom(this, 5);
		ArrayAtom comp7 = new ArrayAtom(this, 5);
		ArrayAtom comp8 = new ArrayAtom(this, 5);
		
		// Sync Components
		Exchange exchange11 = new Exchange(this);
		Exchange exchange12 = new Exchange(this);
		Exchange exchange13 = new Exchange(this);
		Exchange exchange14 = new Exchange(this);
		Exchange exchange21 = new Exchange(this);
		Exchange exchange22 = new Exchange(this);
		Exchange exchange31 = new Exchange(this);


		Finish finish11 = new Finish(this);
		Finish finish12 = new Finish(this);
		Finish finish13 = new Finish(this);
		Finish finish14 = new Finish(this);
		Finish finish21 = new Finish(this);
		Finish finish22 = new Finish(this);
		
		
		
		// Connections
		exchange11.p1.connect(comp1.work);
		exchange11.p2.connect(comp2.work);
		exchange12.p1.connect(comp3.work);
		exchange12.p2.connect(comp4.work);
		exchange13.p1.connect(comp5.work);
		exchange13.p2.connect(comp6.work);
		exchange14.p1.connect(comp7.work);
		exchange14.p2.connect(comp8.work);
		
		
		finish11.p1.connect(comp1.work);
		finish11.p2.connect(comp2.work);
		finish12.p1.connect(comp3.work);
		finish12.p2.connect(comp4.work);
		finish13.p1.connect(comp5.work);
		finish13.p2.connect(comp6.work);
		finish14.p1.connect(comp7.work);
		finish14.p2.connect(comp8.work);
		
		finish21.p1.connect(finish11.work);
		finish21.p2.connect(finish12.work);
		finish22.p1.connect(finish13.work);
		finish22.p2.connect(finish14.work);
		
		exchange21.p1.connect(finish11.work);
		exchange21.p2.connect(finish12.work);
		exchange22.p1.connect(finish13.work);
		exchange22.p2.connect(finish14.work);
		
		exchange31.p1.connect(finish21.work);
		exchange31.p2.connect(finish22.work);
	}
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting3 top = new TestSorting3();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
	
}
