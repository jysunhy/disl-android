package ch.usi.dag.disl.staticcontext;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import ch.usi.dag.disldroidserver.FolderWorker;


public class DexStaticContext extends MethodStaticContext {
	public String getDexName(){
		return FolderWorker.curDex;
	}
	public String getDexShortName(){
		final String fullName = getDexName();
		final String shortName = fullName.substring(fullName.lastIndexOf('/')+1);
		return shortName;
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
