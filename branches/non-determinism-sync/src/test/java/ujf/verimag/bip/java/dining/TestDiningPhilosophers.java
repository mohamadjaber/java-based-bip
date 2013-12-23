package ujf.verimag.bip.java.dining;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestDiningPhilosophers  extends Compound {
	public TestDiningPhilosophers() {
		
		// Base Components
		Philosopher ph1 = new Philosopher(this, 1);
		Philosopher ph2 = new Philosopher(this, 2);


//		Philosopher ph3 = new Philosopher(this, 3);
//		Philosopher ph4 = new Philosopher(this, 4);
		
		// Sync Components
		Fork f1 = new Fork(this, "F1");
		Fork f2 = new Fork(this, "F2");




//		Fork f3 = new Fork(this, "F3");
//		Fork f4 = new Fork(this, "F4");
		
		Eat e0 = new Eat(this, "E0");
//		Eat e1 = new Eat(this, "E1");
		
		// Connections
		f1.rp2.connect(ph2.get);
		f2.rp2.connect(ph2.get);

		f1.rp1.connect(ph1.get);
		f2.rp1.connect(ph1.get);




		
//		f3.rp1.connect(ph3.get);
//		f4.rp2.connect(ph4.get);
//		f4.rp1.connect(ph3.get);
//		f3.rp2.connect(ph4.get);
		
		e0.p2.connect(f2.s);
		e0.p1.connect(f1.s);

		
//		e1.p1.connect(f3.s);
//		e1.p2.connect(f4.s);
	}

	@Test
	public void testMain() throws InterruptedException  {
		TestDiningPhilosophers top = new TestDiningPhilosophers();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
