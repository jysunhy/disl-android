package ch.usi.dag.disl.marker;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.snippet.Shadow.WeavingRegion;
import ch.usi.dag.disl.util.AsmHelper;

/**
 * <p>
 * Marks whole method body.
 * 
 * <p>
 * Sets the start at the beginning of a method and the end at the end of a
 * method. If the method is a constructor, the start is inserted after the
 * constructor invocation.
 */
// FIXME LB: For empty constructors, the order of After and Before snippets is
// reversed.
public class AfterInitBodyMarker extends AbstractMarker {

    @Override
    public List<MarkedRegion> mark(final MethodNode method) {

        MarkedRegion region = new MarkedRegion(
                AsmHelper.findFirstValidMark(method));

        //
        // Add all instructions preceding the RETURN instructions
        // as marked region ends.
        //
        for (final AbstractInsnNode insn : AsmHelper.allInsnsFrom(method.instructions)) {
            int opcode = insn.getOpcode();
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                region.addEnd(insn.getPrevious());
            }
        }

        WeavingRegion wr = region.computeDefaultWeavingRegion(method);
        wr.setAfterThrowEnd(method.instructions.getLast());
        region.setWeavingRegion(wr);

        //

        final List<MarkedRegion> result = new LinkedList<MarkedRegion>();
        result.add(region);
        return result;
    }

}
