package ujf.verimag.bip.java.api;

import ujf.verimag.bip.java.types.WrapType;

public class ReceivePort {
	
	private SyncComponent component; 
	private SendPort sendPort; 
	
	private boolean synced;
	
	public ReceivePort() {
	}
	
	public void connect(SendPort sendPort) {
		this.sendPort = sendPort;
		component.getCompound().getConnections().put(this, sendPort);
		sendPort.getReceivePorts().add(this);
		
		synced = false; 
	}
	
	public SendPort getSendPort() {
		return sendPort;
	}
	
	public void setSynced() {
		synced = true; 
		component.updateSynced(this);
	}
	
	public void reset() {
		synced = false; 
	}
	
	
	public WrapType<Object> getVariable(int index) {
		return sendPort.getVariable(index);
	} 
	
	public void setComponent(SyncComponent component) {
		this.component = component; 
		this.component.getReceivePort().add(this);
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
