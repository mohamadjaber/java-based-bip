package ujf.verimag.bip.java.types;

import java.util.LinkedList;
import java.util.List;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Component;

/**
 * 
 *
 * @param <T>
 */
public class WrapType<T> { 
	
	private Component component; 
	private T originalValue;
	
	/**
	 * Temporary copies of the original value. This is used to handle non-deterministic choice in the sync Components.
	 * Depending on the up actions (non-deterministic transitions) and different values received from the bottom component.  
	 */
	private List<T> copyValues;
		
	
	public WrapType(Component component, T value) {
		this(component);
		originalValue = value; 
	}
	
	
	public WrapType(Component component) {
		copyValues = new LinkedList<T>();
		this.component = component; 
		this.component.addVariable(this);
		originalValue = null;
	}
	
	private void expands() {
		int lastIndex = copyValues.size() - 1;
		while(lastIndex < component.getIndexTransitionEnabled()) {
			copyValues.add(originalValue);
			lastIndex++;
		}
	}
	
	public void updateOriginalValue() {
		int index = component.getIndexTransitionEnabled();
		if(index > 0) {
			assert(index < copyValues.size());
			originalValue = copyValues.get(index);
		}
	}
	
	public T getValue() {
		if(component instanceof BaseComponent)
			return originalValue;
		
		expands();
		return copyValues.get(component.getIndexTransitionEnabled());
	}
	
	
	public void setValue(T value) {
		if(component instanceof BaseComponent) {
			originalValue = value;
			return;
		}
		expands();
		copyValues.set(component.getIndexTransitionEnabled(), value); 
	}

}
