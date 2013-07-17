package ujf.verimag.bip.java.example1;

import java.util.Arrays;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.Location;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.Transition;
import ujf.verimag.bip.java.types.WrapInt;

public class ArrayAtom extends BaseComponent {
	
	// Local data
	private int[] array; 
	private int indexMin, indexMax;
	private int size; 
	
	// Data may attached to ports
	private WrapInt min = new WrapInt();
	private WrapInt max = new WrapInt();
	
	// Location
	private Location l0 = new Location();
	
	// Port
	public SendPort work = new SendPort(min, max);
	
	public ArrayAtom(Compound compound, int size) {
		super(compound);
		setInitial(l0);
		this.size = size; 
		initialization();

		addTransition( new Transition(l0, l0, work) {
			public boolean guard() { return true; }
			public void action() {
				array[indexMin] = min.value;
				array[indexMax] = max.value;
				updateMinMax();	
				System.out.println(this + " : " + Arrays.toString(array));
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
		
		this.min.value = min[0];
		this.max.value = max[0];
		indexMin = min[1];
		indexMax = max[1];		
	}
}
