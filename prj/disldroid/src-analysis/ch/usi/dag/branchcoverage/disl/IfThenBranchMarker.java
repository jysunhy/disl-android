package ch.usi.dag.branchcoverage.disl;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.branchcoverage.util.CodeCoverageUtil;
import ch.usi.dag.disl.marker.AbstractInsnMarker;


public class IfThenBranchMarker extends AbstractInsnMarker {

    @Override
    public List <AbstractInsnNode> markInstruction (final MethodNode methodNode) {

        final List <AbstractInsnNode> selected = new LinkedList <AbstractInsnNode> ();
        final InsnList ilst = methodNode.instructions;

        for (final AbstractInsnNode instruction : ilst.toArray ()) {
            if (CodeCoverageUtil.isCondBranch (instruction)) {
                selected.add (instruction);
            }
        }

        return selected;
    }

}
