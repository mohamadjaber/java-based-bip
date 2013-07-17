package ujf.verimag.bip.java.example1;

public class Primitives {
	
	public static int[] max(int[] array) {
		int[] max = new int[2];
		max[0] = Integer.MIN_VALUE;
		for(int i = 0; i < array.length; i++) {
			if(array[i] > max[0]) {
				max[0] = array[i];
				max[1] = i;
			}
		}
		return max;
	}
	
	public static int[] min(int[] array) {
		int[] min = new int[2];
		min[0] = Integer.MAX_VALUE;
		for(int i = 0; i < array.length; i++) {
			if(array[i] < min[0]) {
				min[0] = array[i];
				min[1] = i;
			}
		}
		return min;
	}

}
