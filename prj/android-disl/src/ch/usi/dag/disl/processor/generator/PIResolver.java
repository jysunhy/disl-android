package ch.usi.dag.disl.processor.generator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.usi.dag.disl.snippet.Shadow;

public class PIResolver {

	private Map<ResolverKey, ProcInstance> piStore = 
		new HashMap<ResolverKey, ProcInstance>();
	
	private static class ResolverKey {
		
		private Shadow shadow;
		private int instrPos;
		
		public ResolverKey(Shadow shadow, int instrPos) {
			super();
			this.shadow = shadow;
			this.instrPos = instrPos;
		}

		@Override
		public int hashCode() {
			
			final int prime = 31;
			int result = 1;
			
			result = prime * result + instrPos;
			
			result = prime * result
					+ ((shadow == null) ? 0 : shadow.hashCode());
			
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			
			if (this == obj)
				return true;
			
			if (obj == null)
				return false;
			
			if (getClass() != obj.getClass())
				return false;
			
			ResolverKey other = (ResolverKey) obj;
			
			if (instrPos != other.instrPos)
				return false;
			
			if (shadow == null) {
				if (other.shadow != null)
					return false;
			} else if (!shadow.equals(other.shadow))
				return false;
			
			return true;
		}
	}
	
	public ProcInstance get(Shadow shadow, int instrPos) {

		ResolverKey key = new ResolverKey(shadow, instrPos);
		
		return piStore.get(key);
	}

	public void set(Shadow shadow, int instrPos,
			ProcInstance processorInstance) {

		ResolverKey key = new ResolverKey(shadow, instrPos);
		
		piStore.put(key, processorInstance);
	}
	
	public Collection<ProcInstance> getAllProcInstances() {
		return piStore.values();
	}
}
