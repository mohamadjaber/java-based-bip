package ujf.verimag.bip.java.post.simple;

public class Configuration {
	
	public static int nbOfClients = 20;
	
	/**
	 * 
	 * @param id
	 * @return a random variable between 0 and n and different than id.
	 */
	public static int randomExcept(int id, int n) {
		int random; 

		do {
			random = (int) (Math.random() * n);
		} while(random == id);
		
		return random; 
	}
	
	
}
