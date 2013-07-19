package ujf.verimag.bip.java.api;

import java.util.Arrays;

/**
 * 
 *
 */
public class TransitionEnabled {
	private TransitionSyncComponent transition;
	private int[] bottomIndices; 
	
	public TransitionEnabled(TransitionSyncComponent t) {
		transition = t;
		bottomIndices = new int[transition.getReceivePorts().length];
	}
	
	
	public TransitionEnabled(TransitionEnabled te) {
		this.transition = te.transition; 
		bottomIndices = Arrays.copyOf(te.bottomIndices, te.bottomIndices.length);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof TransitionEnabled) {
			TransitionEnabled transitionEnabled = (TransitionEnabled) o;
			if(!transitionEnabled.transition.equals(transition))
				return false;
			return Arrays.equals(bottomIndices, transitionEnabled.bottomIndices);
		}
		return super.equals(o);
	}
	
	
	public void updateBottomIndices() {
		for(int i = 0; i < transition.getReceivePorts().length; i++) {
			ReceivePort rcvPort = transition.getReceivePorts()[i];
			Component bottomComponent = rcvPort.getSendPort().getComponent();
			if(bottomComponent instanceof SyncComponent) {
				bottomComponent.setIndexTransitionEnabled(bottomIndices[i]);
			}
		}
	}
	
	
	public int getBottomIndex(ReceivePort rcvPort) {
		assert(transition.getIndexReceivePort(rcvPort) != -1);
		return bottomIndices[transition.getIndexReceivePort(rcvPort)];
	}
	
	public void setBottomIndex(int index, int value) {
		assert(index > 0 && index < bottomIndices.length);
		bottomIndices[index] = value; 
	}

	
	public TransitionSyncComponent getTransition() {
		return transition;
	}
	
	
}
