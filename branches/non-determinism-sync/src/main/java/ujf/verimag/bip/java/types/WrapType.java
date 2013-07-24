package ujf.verimag.bip.java.types;

import java.util.HashMap;
import java.util.Map;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Component;

/**
 * 
 *
 * @param <T>
 */
public class WrapType<T extends Object> { 
	
	private Component component; 
	private T originalValue;
	
	/**
	 * Temporary copies of the original value. This is used to handle non-deterministic choice in the sync Components.
	 * Depending on the up actions (non-deterministic transitions) and different values received from the bottom component.  
	 */	

	private Map<Integer,T> copyValues; 
	
	public WrapType(Component component, T value) {
		this(component);
		originalValue = value; 
	}
	
	
	public WrapType(Component component) {
		copyValues = new HashMap<Integer,T>();
		this.component = component; 
		this.component.addVariable(this);
		originalValue = null;
	}

	
	public void updateOriginalValue() {
		int index = component.getIndexTransitionEnabled();
		if(copyValues.containsKey(index))
			originalValue = copyValues.get(index);
	}
	
	public T getValue() {
		if(component instanceof BaseComponent)
			return originalValue;
		int index = component.getIndexTransitionEnabled();
		if(copyValues.containsKey(index))
			return copyValues.get(index);		
		
	//	System.out.println("OOOPS " + index + "  size:" + copyValues.size());
		return originalValue;
	}
	
	
	
	public void setValue(T value) {
		if(component instanceof BaseComponent) {
			originalValue = value;
			return;
		}
		
	//	if(copyValues.containsKey(component.getIndexTransitionEnabled()))
	//			System.out.println("ooops override");
		copyValues.put(component.getIndexTransitionEnabled(), value);
	}
	
	public void reset() {
		copyValues.clear();
	}

}
