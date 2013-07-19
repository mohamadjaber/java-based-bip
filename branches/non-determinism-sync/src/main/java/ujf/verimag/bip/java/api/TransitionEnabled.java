package ujf.verimag.bip.java.api;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 *
 */
public class TransitionEnabled {
	private TransitionSyncComponent transition;
	private List<Integer> bottomIndices; 
	
	public TransitionEnabled(TransitionSyncComponent t) {
		transition = t;
		bottomIndices = new LinkedList<Integer>();
	}
	
	
	public TransitionEnabled(TransitionEnabled te) {
		this.transition = te.transition; 
		bottomIndices = new LinkedList<Integer>(te.bottomIndices);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof TransitionEnabled) {
			TransitionEnabled keyValues = (TransitionEnabled) o;
			if(!keyValues.transition.equals(transition))
				return false;
			if(bottomIndices.size() != keyValues.bottomIndices.size())
				return false;
			for(int i = 0; i < bottomIndices.size(); i++)
				if(bottomIndices.get(i) != keyValues.bottomIndices.get(i))
					return false;
			return true;
		}
		return super.equals(o);
	}
	
	
	public Integer getBottomIndex(ReceivePort rcvPort) {
		assert(transition.getIndexReceivePort(rcvPort) != -1);
		return bottomIndices.get(transition.getIndexReceivePort(rcvPort));
	}
	
	public List<Integer> getBottomIndices() {
		return bottomIndices;
	}
	
	public TransitionSyncComponent getTransition() {
		return transition;
	}
	
	
}
