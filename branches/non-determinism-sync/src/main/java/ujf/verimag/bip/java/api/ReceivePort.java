package ujf.verimag.bip.java.api;

import ujf.verimag.bip.java.types.WrapType;

public  class ReceivePort {
	
	private SyncComponent component; 
	private SendPort sendPort; 
	
	private boolean synced;
	
	public ReceivePort(SyncComponent component) {
		this.component = component; 
		this.component.getReceivePort().add(this);
		synced = false; 
	}
	
	public void connect(SendPort sendPort) {
		this.sendPort = sendPort;
		component.getCompound().getConnections().put(this, sendPort);
		sendPort.getReceivePorts().add(this);
	}
	
	public synchronized SendPort getSendPort() {
		return sendPort;
	}
	
	public void setSynced(int index) {		
		synced = true; 
		component.updateSynced(this, index);
	}
	
	public void reset() {
		synced = false; 
	}

	
	public synchronized WrapType<?> getVariable(int index) {
		return sendPort.getVariable(index);
	} 

	
	public boolean getSynced() {
		return synced;
	}
	
	public SyncComponent getSyncComponent() {
		return component; 
	}
	
	public Component getComponentBottom() {
		return sendPort.getComponent();
	}
	
}
