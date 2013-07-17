package ujf.verimag.bip.java.types;

import java.util.LinkedList;
import java.util.List;

public abstract class WrapType<T> {
	
	private T originalValue; 
	
	private List<T> copies;
	
	private int currentIndex; 
	
	
	public WrapType(T value) {
		originalValue = value; 
		copies = new LinkedList<T>();
		currentIndex = -1; 
	}
	
	public void setCurrentIndex(int index) {
		currentIndex = index; 
	}
	
	public int getCurrentIndex() {
		return currentIndex; 
	}
	
	public T getValue() {
		int lastIndex = copies.size() - 1;
		while(lastIndex < currentIndex) {
			
			lastIndex++;
		}
		
		
		
		if(copies.size() == currentIndex - 1)
			return copies.get(currentIndex);
		
		
		
		return null;
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
		originalValue = value; 
	}

}
