package ujf.verimag.bip.java.modulo;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestModuloN extends Compound {
	private int nbLevel = 10; // nbLevel = n => modulo 2^n
	
	public TestModuloN() {
		// Base Components
		Generator g = new Generator(this);
		
		// Sync Components
		Modulo2Sync[] syncComponents = new Modulo2Sync[nbLevel];
		
		for(int i = 0; i < nbLevel; i++) {
			syncComponents[i] = new Modulo2Sync(this, "R" + i);
		}
		
		syncComponents[0].r.connect(g.s);
		
		for(int i = 1; i < nbLevel; i++) {
			syncComponents[i].r.connect(syncComponents[i-1].s);
		}
	}
	
	@Test
	public void testMain() throws InterruptedException  {
		TestModuloN top = new TestModuloN();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}

}
