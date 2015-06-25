package ch.usi.dag.netdiagnose.disl;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;


public class InvocationFilterContext extends MethodStaticContext {
    private final String CLASSNAME = "libcore/io/IoBridge";

	public MethodInsnNode getInvocationNode () {
		final StringBuilder builder = new StringBuilder ();
		final AbstractInsnNode instruction = staticContextData.getRegionStart ();

		if (instruction instanceof MethodInsnNode) {
		    return (MethodInsnNode)instruction;
		}

		return null;
	}

	public boolean isConnect(){
	    final MethodInsnNode min = getInvocationNode ();
	    if(min == null) {
            return false;
        }
	    if(!min.owner.equals(CLASSNAME)) {
            return false;
        }
	    if(!min.name.equals ("connect")) {
            return false;
        }
	    System.out.println (min.desc);
        if(!min.desc.equals ("")) {
            return false;
        }
        return true;
	}

	public boolean isSendTo(){
        final MethodInsnNode min = getInvocationNode ();
        if(min == null) {
            return false;
        }
        if(!min.owner.equals(CLASSNAME)) {
            return false;
        }
        if(!min.name.equals ("sendto")) {
            return false;
        }
        System.out.println (min.desc);
        if(!min.desc.equals ("")) {
            return false;
        }
        return true;
    }

	public boolean isSendToByteBuffer(){
        final MethodInsnNode min = getInvocationNode ();
        if(min == null) {
            return false;
        }
        if(!min.owner.equals(CLASSNAME)) {
            return false;
        }
        if(!min.name.equals ("sendto")) {
            return false;
        }
        System.out.println (min.desc);
        if(!min.desc.equals ("")) {
            return false;
        }
        return true;
    }
}
