package ch.usi.dag.disl.staticcontext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;

import ch.usi.dag.disl.snippet.Shadow;
import ch.usi.dag.disl.util.AsmHelper;
import ch.usi.dag.disl.util.cfg.CtrlFlowGraph;

/**
 * <b>NOTE: This class is work in progress</b>
 * <br>
 * <br>
 * Provides static context information about instrumented basic block.
 */
public class BasicBlockStaticContext extends AbstractStaticContext {

	private Map<String, CtrlFlowGraph> cache = new HashMap<String, CtrlFlowGraph>();
	protected CtrlFlowGraph customData;
	
	public void staticContextData(Shadow sa) {

		super.staticContextData(sa);
		
		String key = staticContextData.getClassNode().name
				+ staticContextData.getMethodNode().name
				+ staticContextData.getMethodNode().desc;
		
		customData = cache.get(key);

		if (customData == null) {

			customData = produceCustomData();
			cache.put(key, customData);
		}
	}
	
	/**
	 * Returns total number of basic blocks in a method. 
	 */
	public int getTotBBs() {
		return customData.getNodes().size();
	}

	/**
	 * Returns size of the instrumented basic block. 
	 */
	public int getBBSize() {

		int count = 1;
		AbstractInsnNode start;
		List<AbstractInsnNode> ends;

		start = staticContextData.getRegionStart();
		ends = staticContextData.getRegionEnds();

		while (!ends.contains(start)) {

			if (! AsmHelper.isVirtualInstr(start)) {
				count++;
			}

			start = start.getNext();
		}

		return count;
	}

	/**
	 * Returns index of the instrumented basic block. 
	 */
	public int getBBindex() {
		return customData.getIndex(staticContextData.getRegionStart());
	}

	protected CtrlFlowGraph produceCustomData() {
		return new CtrlFlowGraph(staticContextData.getMethodNode());
	}
}
