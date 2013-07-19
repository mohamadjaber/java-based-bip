package ujf.verimag.bip.java.types;

import java.util.LinkedList;
import java.util.List;

import ujf.verimag.bip.java.api.Component;

/**
 * 
 *
 * @param <T>
 */
public class WrapType<T> { 
	
	private Component component; 
	
	/**
	 * values[0] contains the original value of the variable. 
	 * The other indices contain other copies depending on the up actions  (non-deterministic transitions)
	 * and different values received from the bottom component.  
	 */
	private List<T> copyValues;
		
	
	public WrapType(Component component, T value) {
		this(component);
		// original value.
		copyValues.add(value);
	}
	
	
	public WrapType(Component component) {
		copyValues = new LinkedList<T>();
		copyValues.add(null);
		this.component = component; 
	}
	
	private void expands() {
		int lastIndex = copyValues.size() - 1;
		T originalValue = copyValues.get(0);
		while(lastIndex < component.getIndexTransitionEnabled()) {
			copyValues.add(originalValue);
			lastIndex++;
		}
	}
	
	public T getValue() {
		expands();
		return copyValues.get(component.getIndexTransitionEnabled());
	}
	
	
	public void setValue(T value) {
		expands();
		copyValues.set(component.getIndexTransitionEnabled(), value); 
	}

}
