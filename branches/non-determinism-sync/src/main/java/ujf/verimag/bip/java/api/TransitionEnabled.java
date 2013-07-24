package ujf.verimag.bip.java.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 *
 */
public class TransitionEnabled {
	private TransitionSyncComponent transition;
	private int[] bottomIndices; 
	private boolean isEnabled;
	private SyncComponent syncComponent;
	
	private int size; 
	private List<SyncComponent>bottomSyncComponents;
	
	public TransitionEnabled(TransitionSyncComponent t, SyncComponent syncComponent) {
		transition = t;
		size = transition.getReceivePorts().length;
		bottomIndices = new int[size];
		bottomSyncComponents = new ArrayList<SyncComponent>();
		isEnabled = true; 
		this.syncComponent = syncComponent; 
		setBottomSyncComponents();
	}
	
	
	private void setBottomSyncComponents() {
		Compound compound = syncComponent.getCompound();
		for(SyncComponent syncComponent: compound.getSyncComponents()) {
			if(transition.getReceivePorts().length == bottomSyncComponents.size()) return;
			
			for(ReceivePort rcvPort: transition.getReceivePorts()) {
				if(rcvPort.getSendPort().getComponent().equals(syncComponent)) {
					bottomSyncComponents.add(syncComponent);
					break;
				}
			}
		
		}
	}


	public TransitionEnabled(TransitionEnabled te) {
		this.transition = te.transition; 
		this.size = te.size; 
		this.bottomIndices = Arrays.copyOf(te.bottomIndices, te.bottomIndices.length);
		this.bottomSyncComponents = new ArrayList<SyncComponent>(te.bottomSyncComponents);
		this.isEnabled = te.isEnabled; 
		this.syncComponent = te.syncComponent;
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
		for(int i = 0; i < size; i++) {
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
		assert(index >= 0 && index < bottomIndices.length);
		bottomIndices[index] = value; 
	}
	
	public void disable() {
		isEnabled = false; 
	}
	
	public boolean isEnabled() {
		return isEnabled; 
	}

	
	public TransitionSyncComponent getTransition() {
		return transition;
	}
	
	public void acquireBottomSemaphores() {
		// acquire semaphore in order to avoid deadlock. 
		for(SyncComponent syncComponent: bottomSyncComponents) {
			syncComponent.acquireSemaphore();
		}
	}
	
	public void releaseBottomSemaphores() {
		for(SyncComponent syncComponent: bottomSyncComponents) {
			syncComponent.releaseSemaphore();
		}
	}
	
	
	public String toString() {
		String str = transition + ":  ";
		for(int i = 0; i < bottomIndices.length; i++)
			str += bottomIndices[i] + " - ";
		return str;
	}
	
}
