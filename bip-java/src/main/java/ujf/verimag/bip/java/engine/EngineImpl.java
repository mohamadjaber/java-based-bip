package ujf.verimag.bip.java.engine;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import ujf.verimag.bip.java.api.BaseComponent;
import ujf.verimag.bip.java.api.Compound;
import ujf.verimag.bip.java.api.SendPort;
import ujf.verimag.bip.java.api.SyncComponent;

public class EngineImpl implements Engine {
	private Thread thread; 
	
	private final int nbOfBaseComponents;
	private final Semaphore semaphore;
	
	private Map<BaseComponent, SendPort> componentEnablePort;
	
	private Compound compound; 
	
	private List<SyncComponent> topEnableSyncComponent;

	private List<SyncComponent> topEnableSyncComponentSelected;

	
	
	public EngineImpl(Compound compound) {
		this.compound = compound; 
		nbOfBaseComponents = this.compound.getBaseComponents().size();
		semaphore = new Semaphore(0);
		
		componentEnablePort = new HashMap<BaseComponent, SendPort>();
		
		topEnableSyncComponent = new LinkedList<SyncComponent>();
		topEnableSyncComponentSelected = new LinkedList<SyncComponent>();

		
		for(BaseComponent comp: this.compound.getBaseComponents()) {
			componentEnablePort.put(comp, null);
			comp.setEngine(this);
			comp.getThread().start();
		}
				
		thread = new Thread(this);
		thread.start();
	}
	

	private void waitComponents() {
		try {
			semaphore.acquire(nbOfBaseComponents);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyComponents() {
		for(BaseComponent baseComponent: compound.getBaseComponents()) {
			baseComponent.execute(componentEnablePort.get(baseComponent));
			componentEnablePort.put(baseComponent, null);
		}
	}

	/**
	 * @Override
	 * 
	 */
	public void compute() {
		while(updateTopEnableSyncComponent()) {
			int random = (int) (Math.random() * topEnableSyncComponent.size());
			SyncComponent currentSelected = topEnableSyncComponent.get(random);
			currentSelected.propagateEnablePorts(componentEnablePort);
			topEnableSyncComponentSelected.add(currentSelected);
		}
		
		if(topEnableSyncComponentSelected.size() == 0) {
			for(BaseComponent component: compound.getBaseComponents())
				component.getThread().interrupt();
			thread.interrupt();
		}
		
		compound.resetSyncComponents();
		topEnableSyncComponentSelected.clear();
	}
	
	/**
	 * This method updates the top 
	 * @return
	 */
	private boolean updateTopEnableSyncComponent() {
		topEnableSyncComponent.clear();
		for(SyncComponent syncComponent: compound.getSyncComponents()) {
			if(!topEnableSyncComponentSelected.contains(syncComponent) 
					&& syncComponent.isTop() 
					&& syncComponent.isEnable()) {
				topEnableSyncComponent.add(syncComponent);
			}
		}
		return topEnableSyncComponent.size() > 0;
	}
	
	
	/**
	 * @Overrride
	 */
	public void run() {
		while(true) {
			waitComponents();
			compute();
			if(!thread.isInterrupted())
				notifyComponents();
			else {
				System.out.println("***** deadlock ******");
				return;
			}
		}
	}

	/**
	 * @Override
	 */
	public synchronized void notified() {
		semaphore.release();
	}
	
	public Thread getThread() {
		return thread; 
	}
}
