import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;

public class DumbLoopContext extends AbstractStaticContext {

	public boolean hasLoop() {
		InsnList insList = staticContextData.getMethodNode().instructions;
		for (int i = 0; i < insList.size(); i++) {
			AbstractInsnNode ins = insList.get(i);
			if (ins.getOpcode() == Opcodes.GOTO) {
				return true;
			}
		}
		return false;
	}

}
