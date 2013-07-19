package ujf.verimag.bip.java.example1;


import java.util.LinkedList;
import java.util.List;

public class Test {
	
	public static void main(String[] args) {
		int[][] a = {
				{1,2,3,4},
				{5, 6},
				{7, 8, 9}
		};
		
		allCombinations(a);
		
	}
	
	public static void allCombinations(int[][] a) {
		LinkedList<LinkedList<Integer>> outputs = new LinkedList<LinkedList<Integer>>();
		LinkedList<Integer> soFar = new LinkedList<Integer>();
		allCombinations(a, 0, outputs, soFar);
		

		for(List<Integer> output: outputs) {
			for(Integer i: output)
				System.out.print(i + " - ");
			
			System.out.println();
		}
		
	}
	
	
	private static void allCombinations(int[][] a, int pos, LinkedList<LinkedList<Integer>> outputs, LinkedList<Integer> currentOutput) {
		if(pos == a.length) {
			outputs.add(currentOutput);
			return; 
		}
		for(int i = 0; i != a[pos].length; i++) {
			LinkedList<Integer> currentOutputTmp = new LinkedList<Integer>(currentOutput);
			currentOutputTmp.add(a[pos][i]);
			allCombinations(a, pos + 1, outputs, currentOutputTmp);
		}
		
	}
}
