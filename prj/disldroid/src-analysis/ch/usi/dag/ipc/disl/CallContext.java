package ch.usi.dag.ipc.disl;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class CallContext extends MethodStaticContext {
    public String getType () {
		final AbstractInsnNode instruction = staticContextData.getRegionStart ();

		if (instruction instanceof TypeInsnNode) {
			return ((TypeInsnNode) instruction).desc;
		}

		return "java/lang/Object";
	}

	public String getCallee () {
		final StringBuilder builder = new StringBuilder ();
		final AbstractInsnNode instruction = staticContextData.getRegionStart ();

		if (instruction instanceof MethodInsnNode) {
			final MethodInsnNode min = (MethodInsnNode) instruction;
			builder.append (min.owner);
			builder.append ('.');
			builder.append (min.name);
			builder.append (min.desc);
		}

		return builder.toString ();
	}

}
