package ujf.verimag.bip.java.types;

public class CopyValue<T> {
	
	private T value; 
	private int indexTransition;
	private int indexBottom;
	
	
	public CopyValue(T value, int indexTransition, int indexBottom) {
		this.value = value; 
		this.indexTransition = indexTransition;
		this.indexBottom = indexBottom; 
	}
	
	public T getValue() {
		return value;
	}
	
	public int getIndexTransition() {
		return indexTransition;
	}
	
	public int getIndexBottom() {
		return indexBottom; 
	}
	
	
}
