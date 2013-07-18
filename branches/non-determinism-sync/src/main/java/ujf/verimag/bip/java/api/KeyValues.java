package ujf.verimag.bip.java.api;

import java.util.List;

/**
 * 
 *
 */
public class KeyValues {
	private AbstractTransition transition;
	private List<Integer> bottomIndices; 
	
	public KeyValues(AbstractTransition t, int index) {
	
	}
	

	@Override
	public boolean equals(Object o) {
		if(o instanceof KeyValues) {
			KeyValues keyValues = (KeyValues) o;
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
}
