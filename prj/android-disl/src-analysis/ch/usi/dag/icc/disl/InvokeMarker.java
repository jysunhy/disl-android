package ch.usi.dag.icc.disl;

import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.exception.MarkerException;
import ch.usi.dag.disl.marker.AbstractInsnMarker;
import ch.usi.dag.disl.marker.Parameter;


public class InvokeMarker extends AbstractInsnMarker {

    String methodName;


    public InvokeMarker (final Parameter param) throws MarkerException {
        methodName = param.getValue ();
    }


    @Override
    public List <AbstractInsnNode> markInstruction (final MethodNode methodNode) {
        final List <AbstractInsnNode> seleted = new LinkedList <AbstractInsnNode> ();

        for (final AbstractInsnNode instruction : methodNode.instructions.toArray ()) {

            if (instruction instanceof MethodInsnNode
                && ((MethodInsnNode) instruction).name.equals (methodName)) {
                seleted.add (instruction);
            }
        }

        return seleted;
    }

}
