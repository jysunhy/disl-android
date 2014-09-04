package ch.usi.dag.cc.disl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.disl.marker.AbstractInsnMarker;


public class BranchLabelMarker extends AbstractInsnMarker {

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

        for (final AbstractInsnNode instruction : ilst.toArray ()) {

            if (CodeCoverageUtil.isCondBranch (instruction)) {
                final JumpInsnNode jin = (JumpInsnNode) instruction;

                if (labels.contains (jin.label)) {
                    final LabelNode newLabel = new LabelNode ();
                    ilst.insert (jin.label, newLabel);
                    jin.label = newLabel;
                }

                labels.add (jin.label);
            }
        }

        return new LinkedList <AbstractInsnNode> (labels);
    }

}
