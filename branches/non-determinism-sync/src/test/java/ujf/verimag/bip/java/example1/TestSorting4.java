package ujf.verimag.bip.java.example1;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestSorting4 extends Compound {
	
	private int nbOfAtoms = Configuration.nbOfAtoms; // should be equal 2^n (n >= 2)
	private int sizeLocalArray = 1000;
	
	public TestSorting4() {		
		ArrayAtom[] baseComponents = new ArrayAtom[nbOfAtoms];
		for(int i = 0; i < nbOfAtoms; i++) {
			baseComponents[i] = new ArrayAtom(this, sizeLocalArray, i);
		}
		
		int nbLayerFinishExchange = (int) (Math.log(nbOfAtoms) / Math.log(2)) - 1; 
				
		int nbExchangeFinishPerLayer = nbOfAtoms/2;
		ExchangeFinish[][] syncExchangesFinishes = new ExchangeFinish[ nbLayerFinishExchange ][];

		for(int i = 0; i < nbLayerFinishExchange; i++) {
			syncExchangesFinishes[i] = new ExchangeFinish[nbExchangeFinishPerLayer];
			for(int j = 0; j < nbExchangeFinishPerLayer; j++) {
				syncExchangesFinishes[i][j] = new ExchangeFinish(this);
			}
			nbExchangeFinishPerLayer /= 2;
		}

		for(int i = 0; i < nbOfAtoms/2; i++) {
			syncExchangesFinishes[0][i].p1.connect(baseComponents[2*i].work);
			syncExchangesFinishes[0][i].p2.connect(baseComponents[2*i + 1].work);
		}

		nbExchangeFinishPerLayer = nbOfAtoms/4;
		for(int i = 1; i < nbLayerFinishExchange; i++) {
			for(int j = 0; j < nbExchangeFinishPerLayer; j++) {				
				syncExchangesFinishes[i][j].p1.connect(syncExchangesFinishes[i-1][j*2].work);
				syncExchangesFinishes[i][j].p2.connect(syncExchangesFinishes[i-1][j*2 + 1].work);
			}
			nbExchangeFinishPerLayer /= 2;
		}
		
		Exchange top = new Exchange(this);
		int i = nbLayerFinishExchange - 1; 
		top.p1.connect(syncExchangesFinishes[i][0].work);
		top.p2.connect(syncExchangesFinishes[i][1].work);
	}
	
	
	/*
	public static void main(String[] args) throws InterruptedException {
		TestSorting2 top = new TestSorting2();
		double startTime, stopTime;
		startTime = System.currentTimeMillis();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		stopTime = System.currentTimeMillis();
		System.out.println("Total Time (seconds): " + (stopTime-startTime)/1000);
	}
	*/
	
	
	@Test
	public void testMain() throws InterruptedException {
		TestSorting4 top = new TestSorting4();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
	
	
}

