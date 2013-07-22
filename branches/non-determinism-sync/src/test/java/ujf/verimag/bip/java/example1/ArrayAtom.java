package ujf.verimag.bip.java.example1;

import java.util.Arrays;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapType;

public class ArrayAtom extends BaseComponent {
	
	// Deterministic Local data
	private int[] array; 
	private int indexMin, indexMax;
	private int size; 
	private final int identifier;
	
	// Data may attached to ports
	private WrapType<Integer> min = new WrapType<Integer>(this);
	private WrapType<Integer> max = new WrapType<Integer>(this);
	
	// Location
	private Location l0 = new Location(this);
	
	// Port
	public SendPort work = new SendPort(this, min, max);
	
	public ArrayAtom(Compound compound, int size, int id) {
		super(compound);
		setInitial(l0);
		this.size = size; 
		this.identifier = id;
		initialization();

		addTransition( new Transition(l0, l0, work) {
			public boolean guard() { return true; }
			public void action() {
				array[indexMin] = min.getValue();
				array[indexMax] = max.getValue();
				updateMinMax();	
				System.out.println(identifier + " : " + Arrays.toString(array));
			}
		});
	}
	
	private void initialization() {
		array = new int[size];
		
		for(int i = 0; i < array.length; i++)
			array[i] = (int) (Math.random() * 100);	
		updateMinMax();
	}


	private void updateMinMax() {
		int[] max = Primitives.max(array);
		int[] min = Primitives.min(array);
		
		this.min.setValue(min[0]);
		this.max.setValue(max[0]);
		indexMin = min[1];
		indexMax = max[1];		
	}
}
