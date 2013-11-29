package ch.usi.dag.disl.localvar;

import java.util.HashMap;
import java.util.Map;

public class LocalVars {

	private Map<String, SyntheticLocalVar> syntheticLocals = 
		new HashMap<String, SyntheticLocalVar>();
	private Map<String, ThreadLocalVar> threadLocals = 
		new HashMap<String, ThreadLocalVar>();

	public Map<String, SyntheticLocalVar> getSyntheticLocals() {
		return syntheticLocals;
	}

	public Map<String, ThreadLocalVar> getThreadLocals() {
		return threadLocals;
	}
	
	public void putAll(LocalVars localVars) {
		
		syntheticLocals.putAll(localVars.getSyntheticLocals());
		threadLocals.putAll(localVars.getThreadLocals());
	}
}
