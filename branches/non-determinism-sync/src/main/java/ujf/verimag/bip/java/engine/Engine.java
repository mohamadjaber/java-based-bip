package ujf.verimag.bip.java.engine;

public interface Engine extends Runnable {

	public void notified();
	public void compute();
	public void notifyComponents();

}
