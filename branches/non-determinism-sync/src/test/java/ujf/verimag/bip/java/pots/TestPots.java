package ujf.verimag.bip.java.pots;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.engine.EngineImpl;

public class TestPots extends Compound {

	public TestPots() {
		Client[] clients = new Client[Configuration.nbOfClients];
		for(int i = 0; i < Configuration.nbOfClients; i++)
			clients[i] = new Client(this,i);
		
		CalleeAgregation calleeAgregation = new CalleeAgregation(this);
		CallerAgregation callerAgregation = new CallerAgregation(this);
		CallerCalleeSync callerCalleeSync = new CallerCalleeSync(this);
		
		GeneralAgregation disconnectAgregation1 = new GeneralAgregation(this);
		GeneralAgregation disconnectAgregation2 = new GeneralAgregation(this);
		GeneralSync disconnectSync = new GeneralSync(this);
		
		GeneralAgregation voiceAgregation1 = new GeneralAgregation(this);
		GeneralAgregation voiceAgregation2 = new GeneralAgregation(this);
		GeneralSync voiceSync = new GeneralSync(this);
		
		for(int i = 0; i < Configuration.nbOfClients; i++) {
			calleeAgregation.callees[i].connect(clients[i].wait);
			callerAgregation.callers[i].connect(clients[i].dial);
			disconnectAgregation1.receivers[i].connect(clients[i].disconnect);
			disconnectAgregation2.receivers[i].connect(clients[i].disconnect);
			voiceAgregation1.receivers[i].connect(clients[i].voice);
			voiceAgregation2.receivers[i].connect(clients[i].voice);
		}
		callerCalleeSync.callee.connect(calleeAgregation.callee);
		callerCalleeSync.caller.connect(callerAgregation.caller);
			
		disconnectSync.rcv1.connect(disconnectAgregation1.sender);
		disconnectSync.rcv2.connect(disconnectAgregation2.sender);
		
		voiceSync.rcv1.connect(voiceAgregation1.sender);
		voiceSync.rcv2.connect(voiceAgregation2.sender);
	}
	
	
	@Test
	public void testMain() throws InterruptedException {
		TestPots top = new TestPots();
		EngineImpl engine = new EngineImpl(top);
		engine.getThread().join();
		assertTrue(true);
	}
	
}
