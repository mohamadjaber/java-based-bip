package ujf.verimag.bip.java.example1;



import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;


public class TestSorting2 extends Compound {
	private int nbOfAtoms = Configuration.nbOfAtoms; // should be equal 2^n (n >= 2)
	private int sizeLocalArray = 1000;
	
	public TestSorting2() {		
		ArrayAtom[] baseComponents = new ArrayAtom[nbOfAtoms];
		for(int i = 0; i < nbOfAtoms; i++) {
			baseComponents[i] = new ArrayAtom(this, sizeLocalArray, i);
		}

		int nbLayerExchange = (int) (Math.log(nbOfAtoms)/ Math.log(2));
		int nbLayerFinish = nbLayerExchange - 1; 

				
		int nbExchangePerLayer = nbOfAtoms/2;
		Exchange[][] syncExchanges = new Exchange[ nbLayerExchange ][];
		Finish[][] syncFinishes = new Finish[ nbLayerFinish ][];

		for(int i = 0; i < nbLayerExchange; i++) {
			syncExchanges[i] = new Exchange[nbExchangePerLayer];
			if(i < nbLayerFinish) {
				syncFinishes[i] = new Finish[nbExchangePerLayer];
			}
			for(int j = 0; j < nbExchangePerLayer; j++) {
				syncExchanges[i][j] = new Exchange(this); 
			//	System.out.println("Exchange[" + i +  "]["+ j + "] -> " + syncExchanges[i][j]);

				if(i < nbLayerFinish) {
					syncFinishes[i][j] = new Finish(this);
				//	System.out.println("Finish[" + i +  "]["+j + "] -> " + syncFinishes[i][j]);
				}
			}
			nbExchangePerLayer /= 2;
		}

		for(int i = 0; i < nbOfAtoms/2; i++) {
			syncExchanges[0][i].p1.connect(baseComponents[2*i].work);
			syncExchanges[0][i].p2.connect(baseComponents[2*i + 1].work);
			syncFinishes[0][i].p1.connect(baseComponents[2*i].work);
			syncFinishes[0][i].p2.connect(baseComponents[2*i + 1].work);
		}

		nbExchangePerLayer = nbOfAtoms/4;
		for(int i = 1; i < nbLayerExchange; i++) {
			for(int j = 0; j < nbExchangePerLayer; j++) {				
				syncExchanges[i][j].p1.connect(syncFinishes[i-1][j*2].work);
				syncExchanges[i][j].p2.connect(syncFinishes[i-1][j*2 + 1].work);
				if(i < nbLayerFinish) {
					syncFinishes[i][j].p1.connect(syncFinishes[i-1][j*2].work);
					syncFinishes[i][j].p2.connect(syncFinishes[i-1][j*2 + 1].work);	
				}
			}
			nbExchangePerLayer /= 2;
		}
		
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
		TestSorting2 top = new TestSorting2();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
	
	
}
