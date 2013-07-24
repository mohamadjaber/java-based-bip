package ujf.verimag.bip.java.post.simple;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestPotsSimple extends Compound {

	public TestPotsSimple() {
		Client[] clients = new Client[Configuration.nbOfClients];
		for(int i = 0; i < Configuration.nbOfClients; i++)
			clients[i] = new Client(this,i);
		
		CalleeAgregation calleeAgregation = new CalleeAgregation(this);
		CallerAgregation callerAgregation = new CallerAgregation(this);
		CallerCalleeSync callerCalleeSync = new CallerCalleeSync(this);

		
		for(int i = 0; i < Configuration.nbOfClients; i++) {
			calleeAgregation.callees[i].connect(clients[i].wait);
			callerAgregation.callers[i].connect(clients[i].dial);
		}
		
		
		callerCalleeSync.callee.connect(calleeAgregation.callee);
		callerCalleeSync.caller.connect(callerAgregation.caller);
	}

	
	
	
	@Test
	public void testMain() throws InterruptedException {
		TestPotsSimple top = new TestPotsSimple();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
}
