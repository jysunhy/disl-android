package ch.usi.dag.icc.disl;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.marker.AbstractInsnMarker;


public class LastReturnMarker extends AbstractInsnMarker {

    @Override
    public List <AbstractInsnNode> markInstruction (final MethodNode methodNode) {
        final List <AbstractInsnNode> seleted = new LinkedList <AbstractInsnNode> ();
        AbstractInsnNode ret = null;

        for (final AbstractInsnNode instruction : methodNode.instructions.toArray ()) {
            final int opcode = instruction.getOpcode ();
            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                ret = instruction;
            }
        }

        if (ret != null) {
            seleted.add (ret);
        }

        return seleted;
    }

}
