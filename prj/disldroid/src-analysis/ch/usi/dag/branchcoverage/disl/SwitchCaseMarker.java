package ch.usi.dag.branchcoverage.disl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.disl.marker.AbstractInsnMarker;


public class SwitchCaseMarker extends AbstractInsnMarker {

    @Override
    public List <AbstractInsnNode> markInstruction (final MethodNode methodNode) {

        final Set<LabelNode> labels = new HashSet<LabelNode>();
        final InsnList ilst = methodNode.instructions;

        for (final AbstractInsnNode instruction : ilst.toArray()) {

            switch (instruction.getOpcode()) {
            case Opcodes.LOOKUPSWITCH:
                labels.add(((LookupSwitchInsnNode) instruction).dflt);
                labels.addAll(((LookupSwitchInsnNode) instruction).labels);
                break;

            case Opcodes.TABLESWITCH:
                labels.add(((TableSwitchInsnNode) instruction).dflt);
                labels.addAll(((TableSwitchInsnNode) instruction).labels);
                break;

            default:
                break;
            }

        }
        return new LinkedList <AbstractInsnNode> (labels);
    }

}
