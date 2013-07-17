package ujf.verimag.bip.java.types;

import java.util.LinkedList;
import java.util.List;

import ujf.verimag.bip.java.api.Component;

public class WrapType<T> { 
	
	private Component component; 
	
	/**
	 * values[0] contains the original value of the variables. 
	 * The other indices contain other values depending on the up actions of non-deterministic transitions
	 * and different values received from the bottom component.  
	 */
	private List<CopyValue<T>> copyValues;
		
	
	public WrapType(Component component, T value) {
		this(component);
	}
	
	
	public WrapType(Component component) {
		copyValues = new LinkedList<CopyValue<T>>();
		this.component = component; 
	}
	
	/*
	public void setCurrentIndex(int index) {
		currentIndex = index; 
	}
	
	public int getCurrentIndex() {
		return currentIndex; 
	}
	
	public T getValue() {
		expands();
		return copies.get(currentIndex);
	}
	
	public void update(int index) {
		if(copies.size() > 0) {
			assert(copies.size() > index && index >=0);
			originalValue = copies.get(index);
			copies.clear();
			currentIndex = -1; 
		}
	}
	
	
	public void setValue(T value) {
		expands();
		copies.set(currentIndex, value); 
	}
	
	private void expands() {
		int lastIndex = copies.size() - 1;
		while(lastIndex < currentIndex) {
			copies.add(originalValue);
			lastIndex++;
		}
	}

*/
}
