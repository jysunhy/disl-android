package ch.usi.dag.fia.disl;

import static org.objectweb.asm.tree.AbstractInsnNode.LABEL;
import static org.objectweb.asm.tree.AbstractInsnNode.LINE;

import org.objectweb.asm.tree.AbstractInsnNode;

import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class AllocationSiteStaticContext extends MethodStaticContext {

    /**
     * The character used to separate the components of the allocation site identifier.
     */
    public static final char SUBSEP = '\034';

    public static final int OUT_OF_LINE_INDEX = -1;

    public String getAllocationSite() {
        int index = -1; // zero-based indices

        for (AbstractInsnNode i = staticContextData.getRegionStart(); i != null; i = i.getPrevious()) {
            if (i.getType() != LABEL && i.getType() != LINE) {
                index++;
            }
        }

        return thisClassName() + SUBSEP + thisMethodName() + SUBSEP + thisMethodDescriptor() + SUBSEP + index;
    }

    public String getReflectiveAllocationSite() {
        return thisClassName() + SUBSEP + thisMethodName() + SUBSEP + thisMethodDescriptor() + SUBSEP + OUT_OF_LINE_INDEX;
    }
}
