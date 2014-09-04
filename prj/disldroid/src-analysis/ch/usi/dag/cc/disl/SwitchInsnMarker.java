package ch.usi.dag.cc.disl;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;

import ch.usi.dag.disl.marker.AbstractInsnMarker;

public class SwitchInsnMarker extends AbstractInsnMarker {

	@Override
	public List<AbstractInsnNode> markInstruction(final MethodNode methodNode) {
        final List <AbstractInsnNode> selected = new LinkedList <AbstractInsnNode> ();
        final InsnList ilst = methodNode.instructions;

        for (final AbstractInsnNode instruction : ilst.toArray ()) {

            if (instruction instanceof LookupSwitchInsnNode) {
                selected.add (instruction);
            } else if (instruction instanceof TableSwitchInsnNode) {
                selected.add (instruction);
            }
        }

        return selected;
	}

}
