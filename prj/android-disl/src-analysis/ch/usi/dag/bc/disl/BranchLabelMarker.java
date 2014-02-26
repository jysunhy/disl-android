package ch.usi.dag.bc.disl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.marker.AbstractInsnMarker;

public class BranchLabelMarker extends AbstractInsnMarker {

	@Override
	public List<AbstractInsnNode> markInstruction(final MethodNode methodNode) {

		final Set<LabelNode> labels = new HashSet<LabelNode>();
		final InsnList ilst = methodNode.instructions;

		for (final AbstractInsnNode instruction : ilst.toArray()) {

			if (BCUtil.isCondBranch(instruction)) {
				labels.add(((JumpInsnNode) instruction).label);
			}
		}

		return new LinkedList<AbstractInsnNode>(labels);
	}

}
