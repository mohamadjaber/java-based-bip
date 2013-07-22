package ujf.verimag.bip.java.modulo;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestModulo8  extends Compound {
	public TestModulo8() {
		// Base Components
		Generator g = new Generator(this);
		
		// Sync Components
		Modulo2Sync m0 = new Modulo2Sync(this, "R0");
		Modulo2Sync m1 = new Modulo2Sync(this, "R1");
		Modulo2Sync m2 = new Modulo2Sync(this, "R2");
		
		// Connections
		m0.r.connect(g.s);
		m1.r.connect(m0.s);
		m2.r.connect(m1.s);
	}

	
	@Test
	public void testMain() throws InterruptedException  {
		TestModulo8 top = new TestModulo8();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
